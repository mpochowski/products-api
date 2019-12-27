package com.godaddy.uk.productsapi.services;

import com.godaddy.uk.productsapi.exceptions.InvalidInputException;
import com.godaddy.uk.productsapi.exceptions.NotFoundException;
import com.godaddy.uk.productsapi.exceptions.UnauthorisedException;
import com.godaddy.uk.productsapi.models.LoggedInUser;
import com.godaddy.uk.productsapi.models.database.BrandEntity;
import com.godaddy.uk.productsapi.models.database.UserEntity;
import com.godaddy.uk.productsapi.repositories.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.function.Function;

import static com.godaddy.uk.productsapi.models.UserRole.ROLE_USER;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceTest {

    private static final Function<String, String> REVERSE = text -> new StringBuilder(text).reverse().toString();

    @Mock
    private BrandsService brandsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        // let's reverse all passwords so we know they were encoded
        when(passwordEncoder.encode(anyString())).then(invocation -> REVERSE.apply(invocation.getArgument(0).toString()));
    }

    @Test
    public void testLoadUserByUsername() {
        // given
        String authenticationName = "1-john";
        // given: mock User Entity response from database
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("john");
        userEntity.setPasswordHash("!@£");
        userEntity.setRole(ROLE_USER);
        BrandEntity brandEntity = mock(BrandEntity.class);
        when(brandEntity.getId()).thenReturn(1);
        userEntity.setBrand(brandEntity);
        when(this.usersRepository.findByUsernameAndBrandId("john", 1)).thenReturn(Optional.of(userEntity));

        // when
        LoggedInUser user = this.usersService.loadUserByUsername(authenticationName);

        // then
        assertNotNull(user);
        assertEquals(1, user.getBrandId());
        assertEquals("john", user.getOriginalUsername());
        assertEquals("1-john", user.getUsername());
        assertEquals(1, user.getAuthorities().size());
        assertEquals(ROLE_USER.name(), user.getAuthorities().iterator().next().getAuthority());
        assertEquals("!@£", user.getPassword());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_authenticationStringInIncorrectFormat() {
        // given
        String authenticationName = "1:john";

        // when
        LoggedInUser user = this.usersService.loadUserByUsername(authenticationName);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_userNotFound() {
        // given
        String authenticationName = "1-john";
        // given: mock User Entity response from database
        when(this.usersRepository.findByUsernameAndBrandId("john", 1)).thenReturn(Optional.empty());

        // when
        LoggedInUser user = this.usersService.loadUserByUsername(authenticationName);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testRegisterNewUser() {
        // given
        int brandId = 1;
        String username = "john";
        String password = "pass";
        // given: mock brand
        BrandEntity brandEntity = new BrandEntity();
        when(brandsService.findById(brandId)).thenReturn(brandEntity);
        // given: mock user response
        when(usersRepository.findByUsernameAndBrandId(username, brandId)).thenReturn(Optional.empty());

        // when
        this.usersService.registerNewUser(brandId, username, password);

        // then: verify user was saved and capture user entity
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(usersRepository).save(captor.capture());
        // then: verify user details
        UserEntity user = captor.getValue();
        assertNotNull(user);
        assertEquals("john", user.getUsername());
        assertEquals("ssap", user.getPasswordHash());
        assertEquals(brandEntity, user.getBrand());
        assertEquals(ROLE_USER, user.getRole());
        assertEquals(0, user.getSubscriptions().size());
    }

    @Test(expected = NotFoundException.class)
    public void testRegisterNewUser_brandNotFound() {
        // given
        int brandId = 1;
        String username = "john";
        String password = "pass";
        // given: mock brand
        when(brandsService.findById(brandId)).thenThrow(new NotFoundException("Brand was not found"));

        // when
        this.usersService.registerNewUser(brandId, username, password);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = InvalidInputException.class)
    public void testRegisterNewUser_userAlreadyExists() {
        // given
        int brandId = 1;
        String username = "john";
        String password = "pass";
        // given: mock brand
        BrandEntity brandEntity = new BrandEntity();
        when(brandsService.findById(brandId)).thenReturn(brandEntity);
        // given: mock user response
        when(usersRepository.findByUsernameAndBrandId(username, brandId)).thenReturn(Optional.of(new UserEntity()));

        // when
        this.usersService.registerNewUser(brandId, username, password);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test
    public void testGetLoggedInUser() {
        // given
        LoggedInUser principal = new LoggedInUser(1, "john", "ssap", ROLE_USER);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        // given: mock response from database
        UserEntity userEntity = new UserEntity();
        when(this.usersRepository.findByUsernameAndBrandId("john", 1)).thenReturn(Optional.of(userEntity));

        // when
        UserEntity result = this.usersService.getLoggedInUser(authentication);

        // then
        assertNotNull(result);
        assertEquals(userEntity, result);
    }

    @Test(expected = UnauthorisedException.class)
    public void testGetLoggedInUser_nullAuthentication() {
        // given
        UsernamePasswordAuthenticationToken authentication = null;

        // when
        this.usersService.getLoggedInUser(authentication);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = UnauthorisedException.class)
    public void testGetLoggedInUser_nullPrincipal() {
        // given
        LoggedInUser principal = null;
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "", emptyList());

        // when
        this.usersService.getLoggedInUser(authentication);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

    @Test(expected = UnauthorisedException.class)
    public void testGetLoggedInUser_userNotFound() {
        // given
        LoggedInUser principal = new LoggedInUser(1, "john", "ssap", ROLE_USER);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        // given: mock response from database
        when(this.usersRepository.findByUsernameAndBrandId("john", 1)).thenReturn(Optional.empty());

        // when
        this.usersService.getLoggedInUser(authentication);

        // then
        fail("The test failed, because expected exception to be thrown.");
    }

}