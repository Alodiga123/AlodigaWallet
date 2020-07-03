package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Table(name = "permission")
@NamedQueries({
    @NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p WHERE p.enabled =1"),
    @NamedQuery(name = "Permission.findById", query = "SELECT p FROM Permission p WHERE p.id = :id"),
    @NamedQuery(name = "Permission.findByGroupId", query = "SELECT p FROM Permission p WHERE p.permissionGroup.id = :groupId ORDER BY p.id DESC")})
public class Permission implements Serializable {

    public static String LOG_IN = "User has logged in.";
    public static String LOG_OUT = "User has logged out.";
    public static Long LIST_PROFILES = 1L;
    public static Long ADD_PROFILE = 2L;
    public static Long CHANGE_PROFILE_STATUS = 3L;
    public static Long EDIT_PROFILE = 4L;
    public static Long VIEW_PROFILE = 5L;
    public static Long LIST_ENTERPRISES = 6L;
    public static Long ADD_ENTERPRISE = 7L;
    public static Long EDIT_ENTERPRISE = 8L;
    public static Long VIEW_ENTERPRISE = 9L;
    public static Long CHANGE_ENTERPRISE_STATUS = 10L;
    public static Long LIST_PRODUCTS = 11L;
    public static Long ADD_PRODUCT = 12L;
    public static Long EDIT_PRODUCT = 13L;
    public static Long VIEW_PRODUCT = 14L;
    public static Long CHANGE_PRODUCT_STATUS = 15L;
    public static Long LIST_COUNTRIES = 16L;
    public static Long ADD_COUNTRY = 17L;
    public static Long EDIT_COUNTRY = 18L;
    public static Long VIEW_COUNTRY = 19L;
    public static Long CHANGE_COUNTRY_STATUS = 20L;
    public static Long LIST_USERS = 21L;
    public static Long ADD_USER = 22L;
    public static Long EDIT_USER = 23L;
    public static Long VIEW_USER = 24L;
    public static Long CHANGE_USER_STATUS = 25L;
    public static Long ADMIN_SETTINGS = 26L;
    public static Long REPORT_EXECUTE = 27L;
    public static Long LIST_REPORTS = 28L;
    public static Long ADD_REPORTS = 29L;
    public static Long EDIT_REPORTS = 30L;
    public static Long VIEW_REPORTS = 31L;
    public static Long CHANGE_REPORTS_STATUS = 32L;
    public static Long AUDIT_ACTIONS = 33L;
    public static Long LIST_AUDI = 34L;
    public static Long VIEW_TRANSACTION = 35L;
    public static Long VIEW_COMMISSION = 36L;
    public static Long BALANCE_ADJUSMENT = 37L;
    public static Long LIST_BANK = 38L;
    public static Long ADD_BANK = 39L;
    public static Long EDIT_BANK = 40L;
    public static Long VIEW_BANK = 41L;
    public static Long LIST_CURRENCIES = 42L;
    public static Long ADD_CURRENCIES	= 43L;
    public static Long EDIT_CURRENCIES = 44L;
    public static Long VIEW_CURRENCIES = 45L;
    public static Long LIST_EXCHANGE_RATE = 46L;
    public static Long ADD_EXCHANGE_RATE = 47L;	
    public static Long EDIT_EXCHANGE_RATE = 48L;
    public static Long VIEW_EXCHANGE_RATE = 49L;
    public static Long LIST_OPERATION_BANK = 50L;
    public static Long VIEW_OPERATION_BANK = 51L;

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private boolean enabled;
    private String entity;
    private String name;
    //bi-directional many-to-one association to PermissionData
    @OneToMany(mappedBy = "permission", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<PermissionData> permissionData;
    @ManyToOne
    @JoinColumn(name = "permissionGroupId")
    private PermissionGroup permissionGroup;

    public Permission() {
    }
    public Permission(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionData> getPermissionData() {
        return this.permissionData;
    }

    public void setPermissionData(List<PermissionData> permissionData) {
        this.permissionData = permissionData;
    }

    public PermissionGroup getPermissionGroup() {
        return this.permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public PermissionData getPermissionDataByLanguageId(Long languageId) {
        PermissionData pd = null;
        for (PermissionData pData : this.permissionData) {
            if (pData.getLanguage().getId().equals(languageId)) {
                pd = pData;
                break;
            }
        }
        return pd;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
