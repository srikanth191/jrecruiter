/*
*	http://www.jrecruiter.org
*
*	Disclaimer of Warranty.
*
*	Unless required by applicable law or agreed to in writing, Licensor provides
*	the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
*	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
*	including, without limitation, any warranties or conditions of TITLE,
*	NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are
*	solely responsible for determining the appropriateness of using or
*	redistributing the Work and assume any risks associated with Your exercise of
*	permissions under this License.
*
*/
package org.jrecruiter.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.jrecruiter.dao.ConfigurationDao;
import org.jrecruiter.dao.UserDao;
import org.jrecruiter.dao.RoleDao;
import org.jrecruiter.model.User;
import org.jrecruiter.model.Role;
import org.jrecruiter.model.UserToRole;
import org.jrecruiter.service.UserService;
import org.jrecruiter.service.exceptions.DuplicateUserException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author Dorota Puchala
 * @version $Id$
 */
public class UserServiceImpl implements UserService {

    /**
     *   Initialize Logging.
     */
    public static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    /**
     *   Used for creating the Apache-Velocity-based Email template.
     */
    private VelocityEngine velocityEngine;

    /**
     * Mailsender.
     */
    private MailSender mailSender;

    /**
     * Email message.
     */
    private SimpleMailMessage message;

    /**
     * User Dao.
     */
    private UserDao userDao;

    /**
     * UserRole Dao.
     */
    private RoleDao roleDao;

    /**
     * Access to settings.
     */
    private ConfigurationDao configurationDao;

    /**
     * @param userDao The userDao to set.
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
	 * @param userRoleDao the userRoleDao to set
	 */
	public void setRoleDao(RoleDao userRoleDao) {
		this.roleDao = userRoleDao;
	}

	/**
     * Sets the mail sender.
     * @param mailSender
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     *
     * Sets the Email message.
     *
     * @param message
     */
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    /**
     * Sets the VelocityEngine.
     *
     * @param velocityEngine
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * @param settingsDAO The settingsDAO to set.
     */
    public final void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    public void addUser(User user) throws DuplicateUserException{

        Date registerDate = new Date();
        user.setRegistrationDate(registerDate);
        user.setUpdateDate(registerDate);
        try {

        	Role role = roleDao.getRole("manager");
        	Set<UserToRole> userToRoles = user.getUserToRoles();

        	UserToRole utr = new UserToRole();
        	utr.setRole(role);
        	utr.setUser(user);

        	userToRoles.add(utr);

        	userDao.save(user);
        } catch (DataIntegrityViolationException e){
            LOGGER.warn("addUser() - A DataIntegrityViolationException was thrown - " + e.getMessage());
            throw new DuplicateUserException("User already exists", e);
        }
    }

    public User getUser(String username) {

        return userDao.getUser(username);
    }

    public void updateUser(User user) {
        Date updateDate = new Date();
        user.setUpdateDate(updateDate);
        userDao.update(user);
    }

    public List<User> getAllUsers() {

        return userDao.getAllUsers();
    }

    public void deleteUser(String[] usernameList){
        userDao.deleteUser(usernameList);
    }

    /* (non-Javadoc)
     * @see org.ajug.service.UserServiceInterface#sendPassword(java.lang.String)
     */
    public void sendPassword(User user) {

        final SimpleMailMessage msg = new SimpleMailMessage(this.message);
        msg.setSubject(configurationDao.get("mail.password.subject").getMessageText());
        msg.setFrom(configurationDao.get("mail.from").getMessageText());
        msg.setTo(user.getFirstName() + " " + user.getLastName()
                + "<" + user.getEmail() + ">");

        final Map < String, Object > model = new HashMap < String, Object > ();
        model.put("password", user.getPassword());

        String result = null;
        try {

            result = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, "mail.password.body", model);
        } catch (VelocityException e) {
            e.printStackTrace();
        }
        msg.setText(result);

        //JavaMailSenderImpl sender=(JavaMailSenderImpl) this.mailSender;
        try {
            //JavaMailSenderImpl r = (JavaMailSenderImpl)mailSender;
            //r.getSession().setDebug(true);
            mailSender.send(msg);
        } catch (MailException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }
}