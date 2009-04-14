/**
 *
 */
package org.jrecruiter.dao;

import java.util.Date;

import junit.framework.Assert;

import org.jrecruiter.model.User;
import org.jrecruiter.service.UserService;
import org.jrecruiter.test.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author Gunnar Hillert
 * @version $Id$
 */
public class DaoAuthenticationProviderTest extends BaseTest {

    private @Autowired UserService userService;
    private @Autowired UserDao userDao;

    /**
     *   Initialize Logging.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DaoAuthenticationProviderTest.class);

    @Test
    public void testGetUser() {

        User user = new User();
        user.setUsername("demo44");
        user.setEmail("demo@demo.com");
        user.setFirstName("Demo First Name");
        user.setLastName("Demo Last Name");
        user.setPassword("demo");
        user.setPhone("123456");
        user.setRegistrationDate(new Date());
        userDao.save(user);

        UserDetails user2 = userService.loadUserByUsername("demo44");
        Assert.assertNotNull(user2);

    }

}
