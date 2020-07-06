//package com.alodiga.wallet.bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ejb.Stateless;
//import javax.ejb.TransactionManagement;
//import javax.ejb.TransactionManagementType;
//import javax.persistence.NoResultException;
//import javax.persistence.Query;
//
//import org.apache.log4j.Logger;
//
//import com.alodiga.wallet.common.model.Enterprise;
//import com.alodiga.wallet.common.model.Language;
//import com.alodiga.wallet.common.model.Permission;
//import com.alodiga.wallet.common.model.PermissionData;
//import com.alodiga.wallet.common.model.PermissionGroup;
//import com.alodiga.wallet.common.model.PermissionHasProfile;
//import com.alodiga.wallet.common.model.Profile;
//import com.alodiga.wallet.common.model.ProfileData;
//import com.alodiga.wallet.common.model.User;
//import com.alodiga.wallet.common.genericEJB.EntityManagerWallet;
//import com.alodiga.wallet.respuestas.Response;
//import com.alodiga.wallet.respuestas.ResponseCode;
//import com.alodiga.wallet.rest.request.PermissionDataRequest;
//import com.alodiga.wallet.rest.request.PermissionHasProfileRequest;
//import com.alodiga.wallet.rest.request.PermissionRequest;
//import com.alodiga.wallet.rest.request.ProfileDataRequest;
//import com.alodiga.wallet.rest.request.ProfileRequest;
//import com.alodiga.wallet.rest.response.EnterpriseResponse;
//import com.alodiga.wallet.rest.response.PermissionResponse;
//import com.alodiga.wallet.rest.response.ProfileListResponse;
//import com.alodiga.wallet.rest.response.ProfileResponse;
//import com.alodiga.wallet.rest.response.UserResponse;
//import com.alodiga.wallet.common.utils.QueryConstants;
//
//@Stateless(name = "FsAccessControl", mappedName = "ejb/FsAccessControl")
//@TransactionManagement(TransactionManagementType.CONTAINER)
//public class APIAccessControls extends EntityManagerWallet  {
//
//
//    private static final Logger logger = Logger.getLogger(APIAccessControls.class);
//
//
//    public Response deletePermissionHasProfile(Long profileId) {
//        try {
//        	logger.info( "deletePermissionHasProfile profileid:"+profileId);
//            entityManager.createNamedQuery(QueryConstants.DELETE_PERMISSION_HAS_PROFILE, PermissionHasProfile.class);
//        } catch (Exception e) {
//        	 logger.error( "Error deletePermissionHasProfile");
//             return new Response(ResponseCode.ERROR_INTERNO);
//        }
//        logger.info( "excuted deletePermissionHasProfile profileid:"+profileId);
//        return new Response(ResponseCode.EXITO);
//    }
//    
//    public ProfileListResponse getParentsByProfile(Long profileId) {
//        List<Profile> profiles = null;
//       
//        Query query = null;
//        try {
//        	query = entityManager.createQuery("SELECT php.parent FROM ProfileHasProfile php WHERE php.child.id = ?1 AND php.endingDate IS NULL");
//            query.setParameter("1", profileId);
//            profiles = query.setHint("toplink.refresh", "true").getResultList();
//
//        } catch (Exception e) {
//        	logger.error( "Error deletePermissionHasProfile");
//        	return new ProfileListResponse(ResponseCode.ERROR_INTERNO, "Error getParentsByProfile");
//        }
//        if (profiles.isEmpty()) {
//        	logger.error( "Error deletePermissionHasProfile");
//        	return new ProfileListResponse(ResponseCode.PROFILE_NOT_FOUND, "Pofile not found in getParentsByProfile");
//        }
//        return new ProfileListResponse(ResponseCode.EXITO, "Exito", profiles);
//    }
//    
////    public PermissionListResponse getPermissions() {
////        List<Permission> permissions = new ArrayList<Permission>();
////        try {
////        	logger.info( "getPermissions");
////        	permissions = entityManager.createNamedQuery("Permission.findAll", Permission.class).setHint("toplink.refresh", "true").getResultList();
////
////        } catch (Exception e) {
////        	logger.error( "Error getPermissions");
////			return new PermissionListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissions");
////        }
////        if (permissions.isEmpty()) {
////        	logger.error( "Permissions empty");
////			return new PermissionListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Permissions not founds");
////        }
////        logger.info( "Return permissions founds");
////		return new PermissionListResponse(ResponseCode.EXITO, "Exito", permissions);
////    }
////    
////    public ProfileListResponse getProfiles()  {
////        List<Profile> profiles = new ArrayList<Profile>();
////        try {
////        	profiles = entityManager.createNamedQuery("Profile.findAll", Profile.class).setHint("toplink.refresh", "true").getResultList();
////        } catch (Exception e) {
////        	logger.error( "Error getPermissionByProfileId");
////			return new ProfileListResponse(ResponseCode.ERROR_INTERNO, "Error in getPermissionByProfileId");
////        }
////        if (profiles.isEmpty()) {
////        	logger.error( "Profiles empty");
////			return new ProfileListResponse(ResponseCode.PERMISSION__NOT_FOUND, "Profile not founds");
////        }
////        logger.info( "Return profiles founds");
////		return new ProfileListResponse(ResponseCode.EXITO, "Exito", profiles);
////    }
////    
////    public PermissionResponse loadPermissionById(Long permissionId) {
////    	Permission permission = null;
////		try {
////			logger.info( "Search Permission by id:"+ permissionId);
////			permission = entityManager.createNamedQuery("Permission.findById", Permission.class).setParameter("id", permissionId).getSingleResult();
////		} catch (NoResultException e) {
////			logger.info( "Error Profile not found id:"+permissionId);
////			return new PermissionResponse(ResponseCode.PERMISSION__NOT_FOUND, "Error loadProfileById");
////		}catch (Exception e) {
////			logger.info( "Error loadPermissionById");
////			return new PermissionResponse(ResponseCode.ERROR_INTERNO, "Error loadPermissionById");
////		}
////		logger.info("Permission found id:"+permissionId);
////		return new PermissionResponse(ResponseCode.EXITO, "", permission);
////    }
////    
//    public ProfileResponse loadProfileById(Long profileId) {
//    	Profile profile = null;
//		try {
//			logger.info( "Search Profile by id:"+ profileId);
//			profile = entityManager.createNamedQuery("Profile.findById", Profile.class).setParameter("id", profileId).getSingleResult();
//		} catch (NoResultException e) {
//			logger.info( "Profile not found id:"+profileId);
//			return new ProfileResponse(ResponseCode.PROFILE_NOT_FOUND, "Profile not found id:"+profileId);
//		}catch (Exception e) {
//			logger.info( "Error loadProfileById");
//			return new ProfileResponse(ResponseCode.ERROR_INTERNO, "Error loadProfileById");
//		}
//		logger.info("Profile found id:"+profileId);
//		return new ProfileResponse(ResponseCode.EXITO, "", profile);
//    }
//    
//    public EnterpriseResponse loadEnterpriseById(Long enterpriseId) {
//    	Enterprise enterprise = null;
//		try {
//			logger.info( "Search Enterprise by id:"+ enterpriseId);
//			enterprise = entityManager.createNamedQuery("Enterprise.findById", Enterprise.class).setParameter("id", enterpriseId).getSingleResult();
//		} catch (NoResultException e) {
//			logger.info( "Enterprise not found id:"+enterpriseId);
//			return new EnterpriseResponse(ResponseCode.ENTERPRISE_NOT_FOUND, "Enterprise not found id:"+enterpriseId);
//		}catch (Exception e) {
//			logger.info( "Error loadEnterpriseById");
//			return new EnterpriseResponse(ResponseCode.ERROR_INTERNO, "Error loadEnterpriseById");
//		}
//		logger.info("Enterprise found id:"+enterpriseId);
//		return new EnterpriseResponse(ResponseCode.EXITO, "", enterprise);
//    }
//
//    public Response logginFailed(Object object) { 
//    	return new Response(ResponseCode.ERROR_INTERNO);
//    }
////    
////    
//    public PermissionResponse savePermission(PermissionRequest permissionRequest) {
//    	Permission permission = new Permission();
//		permission.setId(permissionRequest.getId());
//		permission.setAction(permissionRequest.getAction());
//		permission.setEnabled(permissionRequest.isEnabled());
//		permission.setName(permissionRequest.getName());
//		permission.setEntity(permissionRequest.getEntity());	
//		PermissionGroup permissionGroup =  entityManager.find(PermissionGroup.class, permissionRequest.getGroupId());
//		permission.setPermissionGroup(permissionGroup);
//		try {
//			if (permission.getId() != null) {
//				logger.info( "Saving permission");
//				entityManager.merge(permission);
//			} else {
//				logger.info( "Merge permission");
//				entityManager.persist(permission);
//			}
//		} catch (Exception e) {
//			logger.error( "Error saving permission");
//			return new PermissionResponse(ResponseCode.ERROR_INTERNO, "Error saving permission");
//		}
//		logger.info( "Saving Permission Data");
//		List<PermissionData> permissionDatas = new ArrayList<PermissionData>();
//		for (PermissionDataRequest data:permissionRequest.getPermissionDataRequest()) {
//			PermissionData pd = new PermissionData();
//			pd.setId(data.getId());
//			pd.setAlias(data.getAlias());
//			pd.setDescription(data.getDescription());
//			Language language =  entityManager.find(Language.class, data.getLanguageId());
//			pd.setLanguage(language);
//			pd.setPermission(permission);
//			try {
//				if (pd.getId() != null) {
//					logger.info( "Saving permission data");
//					entityManager.merge(pd);
//				} else {
//					logger.info( "Merge permission data");
//					entityManager.persist(pd);
//				}
//				permissionDatas.add(pd);
//			} catch (Exception e) {
//				logger.error( "Error saving permission date");
//				return new PermissionResponse(ResponseCode.ERROR_INTERNO, "Error saving permission data");
//			}
//		}
//		permission.setPermissionData(permissionDatas);
//		logger.info( "Permission saved Completed on success");
//		return new PermissionResponse(ResponseCode.EXITO, "Exito", permission);
//	}
//
//    public ProfileResponse saveProfile(ProfileRequest profileRequest) {
//    	Profile profile = new Profile();
//		profile.setId(profileRequest.getId());
//		profile.setEnabled(profileRequest.isEnabled());
//		profile.setName(profileRequest.getName());
//		try {
//			if (profile.getId() != null) {
//				logger.info( "Saving profile");
//				entityManager.merge(profile);
//			} else {
//				logger.info( "Merge profile");
//				entityManager.persist(profile);
//			}
//		} catch (Exception e) {
//			logger.error( "Error saving profile");
//			return new ProfileResponse(ResponseCode.ERROR_INTERNO, "Error saving profile");
//		}
//		logger.info( "profile saved on success");
//		List<ProfileData> profileDatas = new ArrayList<ProfileData>();
//		for (ProfileDataRequest data:profileRequest.getProfileDataRequests()) {
//			ProfileData profileData = new ProfileData();
//			profileData.setId(data.getId());
//			profileData.setAlias(data.getAlias());
//			profileData.setDescription(data.getDescription());
//			Language language =  entityManager.find(Language.class, data.getLanguageId());
//			profileData.setLanguage(language);
//			profileData.setProfile(profile);
//			try {
//				if (profileData.getId() != null) {
//					logger.info( "Saving profile data");
//					entityManager.merge(profileData);
//				} else {
//					logger.info( "Merge profile data");
//					entityManager.persist(profileData);
//				}
//				profileDatas.add(profileData);
//			} catch (Exception e) {
//				logger.error( "Error saving profile date");
//				return new ProfileResponse(ResponseCode.ERROR_INTERNO, "Error saving profile data");
//			}
//		}
//		profile.setProfileData(profileDatas);
//		List<PermissionHasProfile> phpList = new ArrayList<PermissionHasProfile>();
//		for (PermissionHasProfileRequest request :profileRequest.getPermissionHasProfileRequests()) {
//			PermissionHasProfile php = new PermissionHasProfile();
//			php.setId(request.getId());
//			Permission permission =  entityManager.find(Permission.class, request.getPermissionId());
//			php.setPermission(permission);
//			php.setProfile(profile);
//			try {
//				if (php.getId() != null) {
//					logger.info( "Saving profileHasProfile");
//					entityManager.merge(php);
//				} else {
//					logger.info( "Merge profileHasProfile");
//					entityManager.persist(php);
//				}
//			phpList.add(php);
//			} catch (Exception e) {
//				logger.error( "Error saving profileHasProfile");
//				return new ProfileResponse(ResponseCode.ERROR_INTERNO, "Error saving profileHasProfile");
//			}
//		}
//		profile.setPermissionHasProfiles(phpList);
//		logger.info( "profile saved completed on success");
//		return new ProfileResponse(ResponseCode.EXITO, "Exito", profile);
//	}
//
//    public UserResponse validateUser(String login, String password) {
//        User user = null;
//		try {
//			logger.info( "Validing user by login:"+ login);
//			user = entityManager.createNamedQuery("User.validateUser", User.class).setParameter("login", login).setParameter("password", password).setHint("toplink.refresh", "true").getSingleResult();
//		}catch (NoResultException e) {
//			logger.info( "User not found login:"+login);
//			return new UserResponse(ResponseCode.USER_NOT_FOUND, "User not found login:"+login);
//		} catch (Exception e) {
//			logger.error( "Error User nof found");
//			return new UserResponse(ResponseCode.ERROR_INTERNO, "Error loading user");
//		}
//		if (!user.getEnabled()) {
//			logger.info( "User is disabled:"+login);
//			return new UserResponse(ResponseCode.USER_DISABLED, "User Disabled");
//	    }
//
//		logger.info("User found login:"+login);
//		return new UserResponse(ResponseCode.EXITO, "", user);
//    }
//    
//   }
