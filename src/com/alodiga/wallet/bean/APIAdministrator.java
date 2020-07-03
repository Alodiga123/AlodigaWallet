package com.alodiga.wallet.bean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.alodiga.wallet.genericEJB.AbstractWalletEJB;
import com.alodiga.wallet.genericEJB.EntityManagerWrapper;
import com.alodiga.wallet.model.Permission;
import com.alodiga.wallet.model.PermissionGroup;
import com.alodiga.wallet.model.Profile;
import com.alodiga.wallet.model.User;
import com.alodiga.wallet.respuestas.Response;
import com.alodiga.wallet.respuestas.ResponseCode;
import com.alodiga.wallet.rest.response.PermissionGroupListResponse;
import com.alodiga.wallet.rest.response.PermissionListResponse;
import com.alodiga.wallet.rest.response.PermissionResponse;
import com.alodiga.wallet.rest.response.ProfileListResponse;
import com.alodiga.wallet.rest.response.UserListResponse;
import com.alodiga.wallet.rest.response.UserResponse;
import com.alodiga.wallet.rest.response.ValidateUserResponse;

@Stateless(name = "FsProcessorAdmin", mappedName = "ejb/FsProcessorAdmin")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class APIAdministrator extends AbstractWalletEJB  {


    private static final Logger logger = Logger.getLogger(APIAdministrator.class);


	public UserResponse loadUserByLogin(String login) {
		List<User> users = null;
		logger.info( "Search user by login:"+ login);
		try {
			users = entityManager.createNamedQuery("User.loadUserByLogin", User.class).setParameter("login", login).setHint("toplink.refresh", "true").getResultList();
			if (users.size() <= 0) {
				logger.info( "User nof found");
				return new UserResponse(ResponseCode.USER_NOT_FOUND, "User nof found");
			}

		} catch (Exception e) {
			logger.error("Error loading User");
			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error loading User");
		}
		logger.info( "User found id:"+ users.get(0).getId());
		return new UserResponse(ResponseCode.EXITO, "Exito", users.get(0));
	}

	public UserListResponse getUsers() {
		List<User> users = null;
		try {
			logger.info( "Search users");
			users = entityManager.createNamedQuery("User.findAll", User.class).setHint("toplink.refresh", "true").getResultList();

		} catch (Exception e) {
			logger.info( "Users nof found");
			return new UserListResponse(ResponseCode.ERROR_INTERNO, "Error loading users");
		}
		logger.info( "Users found");
		return new UserListResponse(ResponseCode.EXITO, "Exito", users);

	}

	public UserResponse loadUser(Long userId) {
		User user = null;
		try {
			logger.info( "Search user by id:"+ userId);
			user = entityManager.createNamedQuery("User.findById", User.class).setParameter("id", userId).setHint("toplink.refresh", "true").getSingleResult();
		} catch (NoResultException e) {
			logger.info( "User not found id:"+userId);
			return new UserResponse(ResponseCode.USER_NOT_FOUND, "User not found");
		} catch (Exception e) {
			logger.error( "Error in loadUser");
			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error loading user");
		}
		logger.info("User found id:"+userId);
		return new UserResponse(ResponseCode.EXITO, "", user);

	}

	public UserResponse loadUserByEmail(String email) {
		List<User> users = null;
		try {
			logger.info( "Search user by email:"+ email);
			users = entityManager.createNamedQuery("User.loadUserByEmail", User.class).setParameter("email", email).setHint("toplink.refresh", "true").getResultList();
			if (users.size() <= 0) {
				return new UserResponse(ResponseCode.USER_NOT_FOUND, "User nof found");
			}
		} catch (Exception e) {
			logger.info( "User nof found");
			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error loading User");
		}
		logger.info("User found id:"+users.get(0).getId());
		return new UserResponse(ResponseCode.EXITO, "Exito", users.get(0));

	}

	public UserResponse saveUser(User user) {
		try {
			if (user.getId() != null) {
				logger.info( "Saving User");
				entityManager.merge(user);
			} else {
				logger.info( "Merge User");
				entityManager.persist(user);
			}
		} catch (Exception e) {
			logger.error( "Error saving user");
			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error saving User");
		}
		logger.info( "User saved on success");
		return new UserResponse(ResponseCode.EXITO, "Exito", user);
	}
	
	public UserResponse updateUserPassword(Long userId, String newPassword) {
		User user = null;
		try {
			logger.info( "updatePassword user by id:"+ userId);
			user = entityManager.createNamedQuery("User.findById", User.class).setParameter("id", userId).setHint("toplink.refresh", "true").getSingleResult();
			user.setPassword(newPassword);
			entityManager.merge(user);
		} catch (Exception e) {
			logger.error( "Error updateUserPassword user");
			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error saving User");
		}
		logger.info( "UpdateUserPassword on success");
		return new UserResponse(ResponseCode.EXITO, "Exito", user);
	}

	public ValidateUserResponse validateExistingUser(String login, String email) {
		boolean valid = true;
		UserResponse userResponse = null;
		if (login != null) {
			logger.info( "Validating user login:"+login);
			userResponse = loadUserByLogin(login);

		} else if (email != null) {
			logger.info( "Validating user email:"+email);
			userResponse = loadUserByEmail(email);
		} else {
			return new ValidateUserResponse(ResponseCode.USER_NOT_FOUND, "User nof found");
		}
		if (userResponse.getCodigoRespuesta().equals(ResponseCode.EXITO))
			valid = true;
		logger.info( "Validating user:"+valid);
		return new ValidateUserResponse(ResponseCode.EXITO, "Exito", valid);
	}

	public UserListResponse getUserTopUpNotification() {
		List<User> users = null;
		try {
			logger.info( "Search users with flag TopUpNotification");
			Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.receiveTopUpNotification = TRUE");
			users = query.setHint("toplink.refresh", "true").getResultList();
		} catch (NoResultException ex) {
			logger.error( "Error getUserTopUpNotification");
			return new UserListResponse(ResponseCode.USER_NOT_FOUND, "No user found for TopUp Notifications");
		} catch (Exception e) {
			logger.error( "Error getUserTopUpNotification");
			return new UserListResponse(ResponseCode.ERROR_INTERNO, "Error getUserTopUpNotification");
		}
		logger.info( "Return users with flag TopUpNotification");
		return new UserListResponse(ResponseCode.EXITO, "Exito", users);
	}

    public Response updateUserNotifications(String ids)  {
        try {
        	logger.info( "Update Users TopUpNotification:"+ids);
            Query queryDisable = entityManager.createQuery("UPDATE User u SET u.receiveTopUpNotification = FALSE");
            try {
                queryDisable.executeUpdate();
                if (!ids.equals("")) {
                    Query queryEnable = entityManager.createQuery("UPDATE User u SET u.receiveTopUpNotification = TRUE WHERE u.id IN (" + ids + ")");
                    queryEnable.executeUpdate();
                }
            } catch (Exception ex) {
                logger.error( "Error updateUserNotifications");
                return new Response(ResponseCode.ERROR_INTERNO);
            }
        } catch (Exception e) {
        	logger.error( "Error updateUserNotifications");
        	return new Response(ResponseCode.ERROR_INTERNO);
        }
        return new Response(ResponseCode.EXITO);

    }

    public PermissionGroupListResponse getPermissionGroups()  {
        List<PermissionGroup> permissionGroups = new ArrayList<PermissionGroup>();
        try {
        	logger.info( "getPermissionGroups");
        	permissionGroups = entityManager.createNamedQuery("PermissionGroup.findAll", PermissionGroup.class).setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
        	logger.error( "Error getPermissionGroups");
			return new PermissionGroupListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissionGroups");
        }
        if (permissionGroups.isEmpty()) {
        	logger.error( "PermissionGroups empty");
			return new PermissionGroupListResponse(ResponseCode.PERMISSION_GROUP_NOT_FOUND, "Permission groups not founds");
        }
        logger.info( "Return permission groups founds");
		return new PermissionGroupListResponse(ResponseCode.EXITO, "Exito", permissionGroups);

    }

    public PermissionListResponse getPermissions() {
        List<Permission> permissions = new ArrayList<Permission>();
        try {
        	logger.info( "getPermissions");
        	permissions = entityManager.createNamedQuery("Permission.findAll", Permission.class).setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
        	logger.error( "Error getPermissions");
			return new PermissionListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissions");
        }
        if (permissions.isEmpty()) {
        	logger.error( "Permissions empty");
			return new PermissionListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Permissions not founds");
        }
        logger.info( "Return permissions founds");
		return new PermissionListResponse(ResponseCode.EXITO, "Exito", permissions);
    }

    public PermissionListResponse getPermissionByGroupId(Long groupId)  {
        List<Permission> permissions = new ArrayList<Permission>();
        
        try {
        	logger.info( "getPermissionByGroupId:"+groupId);
        	permissions = entityManager.createNamedQuery("Permission.findByGroupId", Permission.class)
                    .setParameter("groupId", groupId).setHint("toplink.refresh", "true").getResultList();
        } catch (Exception e) {
        	logger.error( "Error getPermissionByGroupId");
			return new PermissionListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissionByGroupId");
        }
        if (permissions.isEmpty()) {
        	logger.error( "Permissions empty");
			return new PermissionListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Permissions not founds");
        }
        logger.info( "Return permissions founds");
		return new PermissionListResponse(ResponseCode.EXITO, "Exito", permissions);
    }

    public PermissionListResponse getPermissionByProfileId(Long profileId)  {

        List<Permission> permissions = new ArrayList<Permission>();
        Query query = null;
        try {
        	logger.info( "getPermissionByProfileId:"+profileId);
            query = entityManager.createQuery("SELECT php.permission FROM PermissionHasProfile php WHERE php.profile.id = ?1");
            query.setParameter("1", profileId);
            permissions = query.setHint("toplink.refresh", "true").getResultList();

        } catch (Exception e) {
        	logger.error( "Error getPermissionByProfileId");
			return new PermissionListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissionByProfileId");
        }
        if (permissions.isEmpty()) {
        	logger.error( "Permissions empty");
			return new PermissionListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Permissions not founds");
        }
        logger.info( "Return permissions founds");
		return new PermissionListResponse(ResponseCode.EXITO, "Exito", permissions);
    }

    public PermissionResponse loadPermissionById(Long permissionId) {
    	Permission permission = null;
		try {
			logger.info( "Search Permission by id:"+ permissionId);
			permission = entityManager.createNamedQuery("Permission.findById", Permission.class).setParameter("id", permissionId).getSingleResult();
		} catch (Exception e) {
			logger.info( "Permission nof found");
			return new PermissionResponse(ResponseCode.ERROR_INTERNO, "Error loadPermissionById");
		}
		logger.info("Permission found id:"+permissionId);
		return new PermissionResponse(ResponseCode.EXITO, "", permission);
    }

    public ProfileListResponse getProfiles()  {
        List<Profile> profiles = new ArrayList<Profile>();
        try {
        	profiles = entityManager.createNamedQuery("Profile.findAll", Profile.class).setHint("toplink.refresh", "true").getResultList();
        } catch (Exception e) {
        	logger.error( "Error getPermissionByProfileId");
			return new ProfileListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissionByProfileId");
        }
        if (profiles.isEmpty()) {
        	logger.error( "Profiles empty");
			return new ProfileListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Profile not founds");
        }
        logger.info( "Return profiles founds");
		return new ProfileListResponse(ResponseCode.EXITO, "Exito", profiles);
    }



   }
