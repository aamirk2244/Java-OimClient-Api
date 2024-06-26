package com.example;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcOrganizationNotFoundException;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.tcResultSet;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import java.util.*;
import java.util.Optional;

public class Users {
    private OIMClient oimClient;
    public Users(OIMClient oimClient){
        this.oimClient = oimClient;
    }
    
    public void create(HashMap<String, Object> userAttributes)throws Exception {

        System.out.println("good");
//        createAttributes.put("User Login", "H11User");
//        createAttributes.put("First Name", "H11 FIRST");
//        createAttributes.put("Last Name","H11 LAST");
        userAttributes.put("act_key", 1l);
//        createAttributes.put(oracle.iam.identity.utils.Constants.PASSWORD, "Welcome1$2");
        userAttributes.put("Xellerate Type", "End-User");
        userAttributes.put("Role", "Full-Time");
        
        
        Hashtable<String, String> env = new Hashtable<String, String>();
        
        UserManager usrMgr = oimClient.getService(UserManager.class);
        UserManagerResult userResult = null;
        String userKey = null;
      

//        userResult = usrMgr.create(new User(null,user_params()));
        System.out.println("fine...");
        userResult = usrMgr.create(new User(null, userAttributes));
        System.out.println("userresult is " + userResult);
        userKey = userResult.getEntityId();
        System.out.println("Created user with key = " + userKey);
    }
    
    public List getUserLogin(String psFirstName) {
          Vector mvUsers = new Vector();
          UserManager userService = oimClient.getService(UserManager.class); 
          Set<String> retAttrs = new HashSet<String>();
    
          // Attributes that should be returned as part of the search. 
          // Retrieve "User Login" attribute of the User.
          // Note: Additional attributes can be specified in a 
          // similar fashion.
          retAttrs.add(AttributeName.USER_LOGIN.getId());
    
          // Construct a search criteria. This search criteria states 
          // "Find User(s) whose 'First Name' equals 'psFirstName'".  
          SearchCriteria criteria;
          criteria = new SearchCriteria(AttributeName.FIRSTNAME.getId(), psFirstName, SearchCriteria.Operator.EQUAL);
          try {
              // Use 'search' method of UserManager API to retrieve 
              // records that match the search criteria. The return 
              // object is of type User. 
              List<User> users = userService.search(criteria, retAttrs, null);
                          
              for (int i = 0; i < users.size(); i++) {
                  //Print User First Name and Login ID 
                  System.out.println("First Name : " + psFirstName + "  --  Login ID : " + users.get(i).getLogin());
                  mvUsers.add(users.get(i).getLogin());
              }
          } catch (AccessDeniedException ade) {
              // handle exception
          } catch (UserSearchException use) {
              // handle exception
          }
         return mvUsers;
      }
    
    /** 
        * Retrieves the administrators of an Organization based on the 
        * Organization name. This is Legacy service API usage. 
        */
       public List getAdministratorsOfOrganization(String psOrganizationName) {
           Vector mvOrganizations = new Vector();
           tcOrganizationOperationsIntf moOrganizationUtility = oimClient.getService(tcOrganizationOperationsIntf.class);
           Hashtable mhSearchCriteria = new Hashtable();
           mhSearchCriteria.put("Organizations.Organization Name", psOrganizationName);
           try {
               tcResultSet moResultSet = moOrganizationUtility.findOrganizations(mhSearchCriteria);
               tcResultSet moAdmins;
               for (int i = 0; i < moResultSet.getRowCount(); i++) {
                   moResultSet.goToRow(i);
                   moAdmins = moOrganizationUtility.getAdministrators(moResultSet.getLongValue("Organizations.Key"));
                   mvOrganizations.add(moAdmins.getStringValue("Groups.Group Name"));
                   System.out.println("Organization Admin Name : " + moAdmins.getStringValue("Groups.Group Name"));
               }
           } catch (tcAPIException tce) {
               // handle exception
           } catch (tcColumnNotFoundException cnfe) {
               // handle exception
           } catch (tcOrganizationNotFoundException onfe) {
               // handle exception
           }
           return mvOrganizations;
       }
    
    public void findAll() throws Exception {
           
        UserManager usrMgr = oimClient.getService(UserManager.class);
        SearchCriteria criteria = new SearchCriteria("User Login","*", SearchCriteria.Operator.EQUAL);
           
        List<User> users = usrMgr.search(criteria, retColumns(), null);
        System.out.println(users);
//
//           for(User user : users) {
//               System.out.println("********COLUMNS********" + user.getAttributeNames());
//               
//               Long usrKey = (Long)user.getAttribute("usr_key");
//                 System.out.println("UserKey-> " +  usrKey);
//               System.out.println(user);
//           
//       }
    }

    
    public Optional<List<User>> findBy(String column, String query) {
           try{
               UserManager usrMgr = oimClient.getService(UserManager.class);

               SearchCriteria criteria = new SearchCriteria(column,
                       query, SearchCriteria.Operator.EQUAL);

               List<User> users = usrMgr.search(criteria, retColumns(), null);
               if(users.isEmpty()) {
                   return Optional.empty();
               }

               return Optional.of(users);

           }catch (Exception e){
               throw new RuntimeException("Failed to search for user", e);
           }
       }
    
    public User findByUserLogin(String query) {

          Optional<List<User>> oimFilteredUsers =  findBy("User Login", query);
          if(oimFilteredUsers.isPresent()) {
              List<User> oimUsers =  oimFilteredUsers.get();
              return oimUsers.get(0);
          }

          return null;
    }

    public void findByFirstName(String query) throws Exception {
        findBy("First Name", query);
    }

    public void findByLastName(String query) throws Exception {
        findBy("Last Name", query);
    }
    
    
    private HashMap<String, Object> user_params() {
    
        HashMap<String, Object> createAttributes = new HashMap<String, Object>();

        createAttributes.put("User Login", "H11User");
        createAttributes.put("First Name", "H11 FIRST");
        createAttributes.put("Last Name","H11 LAST");
        createAttributes.put("act_key", 1l);
        createAttributes.put(oracle.iam.identity.utils.Constants.PASSWORD, "Welcome1$2");
        createAttributes.put("Xellerate Type", "End-User");
        createAttributes.put("Role", "Full-Time");
        return createAttributes;

    }
    
    private Set retColumns() {
        Set retSet = new HashSet();
        retSet.add("usr_key");
        retSet.add("User Login");
        retSet.add("First Name");
        retSet.add("Last Name");
        return retSet;
    }
    
}
