package edu.asu.cse360.hw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for role-based access control functionality.
 * 
 * <p>This test class validates the complete role-based access control (RBAC) system
 * ensuring that users can only access features and perform actions appropriate to
 * their assigned roles. The test suite covers all user roles and their corresponding
 * permissions across different application features.</p>
 * 
 * <p>The access control test covers:</p>
 * <ul>
 *   <li><strong>Permission Validation:</strong> Role-specific feature access verification</li>
 *   <li><strong>Security Enforcement:</strong> Unauthorized access prevention</li>
 *   <li><strong>Role Hierarchy:</strong> Inherited permissions and role relationships</li>
 *   <li><strong>Feature Protection:</strong> Critical operation access control</li>
 *   <li><strong>Session Validation:</strong> Role verification during user sessions</li>
 * </ul>
 * 
 * <p>This test implementation ensures comprehensive coverage of all security
 * scenarios to prevent unauthorized access and maintain system integrity.</p>
 * 
 * @author [Your Name]
 * @version 1.0.0
 * @since 2025-11-02
 * 
 * @see RoleBasedAccessController
 * @see UserRole
 * @see AccessPermission
 * 
 * @testCase Test 4: Role-Based Access Control Test
 * @precondition Access control system and role definitions are properly configured
 * @postcondition All access control scenarios return appropriate authorization results
 * @expectedResult Authorized access succeeds, unauthorized access is blocked with proper error handling
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Role-Based Access Control Test Suite")
public class RoleBasedAccessControlTest {
    
    /** Mock user session for testing */
    @Mock
    private UserSession mockUserSession;
    
    /** Mock permission repository for access rules */
    @Mock
    private PermissionRepository mockPermissionRepository;
    
    /** The access control system under test */
    private RoleBasedAccessController accessController;
    
    /** Test user data for different roles */
    private static final String STUDENT_USERNAME = "student1";
    private static final String ADMIN_USERNAME = "admin1";
    private static final String REVIEWER_USERNAME = "reviewer1";
    private static final String STAFF_USERNAME = "staff1";
    
    /**
     * Sets up the test environment before each test method.
     * 
     * <p>This method initializes all mock objects and creates a fresh instance
     * of the RoleBasedAccessController with mocked dependencies. This ensures
     * test isolation and consistent access control behavior across all test cases.</p>
     * 
     * @testCase Initialize access controller and mocks before each test
     * @precondition Test framework and Mockito are properly configured
     * @postcondition Fresh access controller instance with mocked dependencies
     * @expectedResult Access controller is ready for testing with isolated permission system
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accessController = new RoleBasedAccessController(mockPermissionRepository);
        
        // Setup default permission repository behavior
        setupDefaultPermissions();
    }
    
    /**
     * Sets up default permission mappings for all user roles.
     * 
     * <p>This helper method configures the mock permission repository with
     * standard role-based permissions that reflect the application's security
     * model. These permissions are used as the baseline for access control testing.</p>
     */
    private void setupDefaultPermissions() {
        // Student permissions
        when(mockPermissionRepository.hasPermission(UserRole.STUDENT, AccessPermission.VIEW_QUESTIONS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STUDENT, AccessPermission.SUBMIT_QUESTIONS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STUDENT, AccessPermission.VIEW_ANSWERS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STUDENT, AccessPermission.SUBMIT_ANSWERS)).thenReturn(true);
        
        // Admin permissions (full access)
        when(mockPermissionRepository.hasPermission(eq(UserRole.ADMIN), any(AccessPermission.class))).thenReturn(true);
        
        // Reviewer permissions
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, AccessPermission.VIEW_QUESTIONS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, AccessPermission.VIEW_ANSWERS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, AccessPermission.EDIT_QUESTIONS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, AccessPermission.EDIT_ANSWERS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, AccessPermission.APPROVE_CONTENT)).thenReturn(true);
        
        // Staff permissions
        when(mockPermissionRepository.hasPermission(UserRole.STAFF, AccessPermission.VIEW_QUESTIONS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STAFF, AccessPermission.VIEW_ANSWERS)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STAFF, AccessPermission.MODERATE_CONTENT)).thenReturn(true);
        when(mockPermissionRepository.hasPermission(UserRole.STAFF, AccessPermission.MANAGE_USERS)).thenReturn(true);
    }
    
    /**
     * Tests student role access permissions for basic features.
     * 
     * <p>This test validates that students have appropriate access to view and
     * submit questions and answers while being restricted from administrative
     * functions. Students should have read/write access to content but not
     * moderation or management capabilities.</p>
     * 
     * @param permission the permission to test for student access
     * @param shouldHaveAccess whether students should have this permission
     * 
     * @testCase Verify student role has appropriate permissions
     * @precondition Access controller is configured with student permissions
     * @postcondition Student access is properly granted or denied based on permission type
     * @expectedResult Students can access content features but not administrative functions
     */
    @ParameterizedTest(name = "Student access to {0}: {1}")
    @CsvSource({
        "VIEW_QUESTIONS, true",
        "SUBMIT_QUESTIONS, true",
        "VIEW_ANSWERS, true", 
        "SUBMIT_ANSWERS, true",
        "EDIT_QUESTIONS, false",
        "DELETE_QUESTIONS, false",
        "MANAGE_USERS, false",
        "MODERATE_CONTENT, false",
        "ADMIN_PANEL, false"
    })
    @DisplayName("Test Student Role Access Permissions")
    void testStudentRoleAccess(AccessPermission permission, boolean shouldHaveAccess) {
        // Setup student user session
        when(mockUserSession.getUsername()).thenReturn(STUDENT_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.STUDENT);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Test access permission
        AccessResult result = accessController.checkAccess(mockUserSession, permission);
        
        if (shouldHaveAccess) {
            assertThat(result.isGranted())
                .as("Student should have access to %s", permission)
                .isTrue();
                
            assertThat(result.getDenialReason())
                .as("Granted access should not have denial reason")
                .isNull();
        } else {
            assertThat(result.isGranted())
                .as("Student should not have access to %s", permission)
                .isFalse();
                
            assertThat(result.getDenialReason())
                .as("Denied access should include denial reason")
                .isNotNull()
                .contains("insufficient permissions");
        }
    }
    
    /**
     * Tests admin role access permissions for all system features.
     * 
     * <p>This test validates that administrators have comprehensive access to
     * all system features and administrative functions. Admins should be able
     * to perform any operation within the system without restrictions.</p>
     * 
     * @param permission the permission to test for admin access
     * 
     * @testCase Verify admin role has full system access
     * @precondition Access controller recognizes admin privileges
     * @postcondition Admin has access to all tested permissions
     * @expectedResult All permissions are granted for admin users
     */
    @ParameterizedTest(name = "Admin access to {0}")
    @EnumSource(AccessPermission.class)
    @DisplayName("Test Admin Role Full Access Permissions")
    void testAdminRoleFullAccess(AccessPermission permission) {
        // Setup admin user session
        when(mockUserSession.getUsername()).thenReturn(ADMIN_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.ADMIN);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Test access permission
        AccessResult result = accessController.checkAccess(mockUserSession, permission);
        
        // Admin should have access to everything
        assertThat(result.isGranted())
            .as("Admin should have access to %s", permission)
            .isTrue();
            
        assertThat(result.getDenialReason())
            .as("Admin access should never be denied")
            .isNull();
    }
    
    /**
     * Tests reviewer role access permissions for content management.
     * 
     * <p>This test validates that reviewers have appropriate permissions for
     * content review and moderation tasks while being restricted from user
     * management and system administration functions.</p>
     * 
     * @param permission the permission to test for reviewer access
     * @param shouldHaveAccess whether reviewers should have this permission
     * 
     * @testCase Verify reviewer role has content management permissions
     * @precondition Access controller is configured with reviewer permissions
     * @postcondition Reviewer access is properly granted for content operations
     * @expectedResult Reviewers can manage content but not users or system settings
     */
    @ParameterizedTest(name = "Reviewer access to {0}: {1}")
    @CsvSource({
        "VIEW_QUESTIONS, true",
        "VIEW_ANSWERS, true",
        "EDIT_QUESTIONS, true",
        "EDIT_ANSWERS, true",
        "APPROVE_CONTENT, true",
        "DELETE_QUESTIONS, false",
        "MANAGE_USERS, false",
        "ADMIN_PANEL, false",
        "SYSTEM_SETTINGS, false"
    })
    @DisplayName("Test Reviewer Role Content Management Permissions")
    void testReviewerRoleAccess(AccessPermission permission, boolean shouldHaveAccess) {
        // Setup reviewer user session
        when(mockUserSession.getUsername()).thenReturn(REVIEWER_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.REVIEWER);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Configure specific reviewer permissions
        when(mockPermissionRepository.hasPermission(UserRole.REVIEWER, permission)).thenReturn(shouldHaveAccess);
        
        // Test access permission
        AccessResult result = accessController.checkAccess(mockUserSession, permission);
        
        assertThat(result.isGranted())
            .as("Reviewer access to %s should be %s", permission, shouldHaveAccess)
            .isEqualTo(shouldHaveAccess);
    }
    
    /**
     * Tests staff role access permissions for user management.
     * 
     * <p>This test validates that staff members have appropriate permissions
     * for user management and content moderation while being restricted from
     * administrative system functions and sensitive operations.</p>
     * 
     * @param permission the permission to test for staff access
     * @param shouldHaveAccess whether staff should have this permission
     * 
     * @testCase Verify staff role has user management permissions
     * @precondition Access controller is configured with staff permissions
     * @postcondition Staff access is properly granted for user management operations
     * @expectedResult Staff can manage users and moderate content but not access admin functions
     */
    @ParameterizedTest(name = "Staff access to {0}: {1}")
    @CsvSource({
        "VIEW_QUESTIONS, true",
        "VIEW_ANSWERS, true",
        "MODERATE_CONTENT, true",
        "MANAGE_USERS, true",
        "EDIT_QUESTIONS, false",
        "DELETE_QUESTIONS, false",
        "ADMIN_PANEL, false",
        "SYSTEM_SETTINGS, false"
    })
    @DisplayName("Test Staff Role User Management Permissions")
    void testStaffRoleAccess(AccessPermission permission, boolean shouldHaveAccess) {
        // Setup staff user session
        when(mockUserSession.getUsername()).thenReturn(STAFF_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.STAFF);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Configure specific staff permissions
        when(mockPermissionRepository.hasPermission(UserRole.STAFF, permission)).thenReturn(shouldHaveAccess);
        
        // Test access permission
        AccessResult result = accessController.checkAccess(mockUserSession, permission);
        
        assertThat(result.isGranted())
            .as("Staff access to %s should be %s", permission, shouldHaveAccess)
            .isEqualTo(shouldHaveAccess);
    }
    
    /**
     * Tests access denial for unauthenticated users.
     * 
     * <p>This test validates that unauthenticated users are properly denied
     * access to all protected features regardless of the permission being
     * requested. The system should require valid authentication before
     * evaluating role-based permissions.</p>
     * 
     * @param permission the permission to test for unauthenticated access
     * 
     * @testCase Verify unauthenticated users are denied all access
     * @precondition Access controller enforces authentication requirements
     * @postcondition All permissions are denied for unauthenticated users
     * @expectedResult No access granted without valid authentication
     */
    @ParameterizedTest(name = "Unauthenticated access to {0}")
    @EnumSource(value = AccessPermission.class, names = {"VIEW_QUESTIONS", "SUBMIT_QUESTIONS", "ADMIN_PANEL", "MANAGE_USERS"})
    @DisplayName("Test Access Denial for Unauthenticated Users")
    void testUnauthenticatedAccessDenial(AccessPermission permission) {
        // Setup unauthenticated session
        when(mockUserSession.isAuthenticated()).thenReturn(false);
        when(mockUserSession.getUsername()).thenReturn(null);
        when(mockUserSession.getUserRole()).thenReturn(null);
        
        // Test access permission
        AccessResult result = accessController.checkAccess(mockUserSession, permission);
        
        // All access should be denied
        assertThat(result.isGranted())
            .as("Unauthenticated user should be denied access to %s", permission)
            .isFalse();
            
        assertThat(result.getDenialReason())
            .as("Denial reason should indicate authentication required")
            .contains("authentication required")
            .containsIgnoringCase("login");
    }
    
    /**
     * Tests session validation and timeout handling.
     * 
     * <p>This test validates that the access control system properly validates
     * user sessions and handles session timeouts. Expired sessions should be
     * treated as unauthenticated regardless of stored role information.</p>
     * 
     * @testCase Verify session validation and timeout handling
     * @precondition Access controller validates session state
     * @postcondition Expired sessions are denied access appropriately
     * @expectedResult Access denied for expired sessions with timeout message
     */
    @Test
    @DisplayName("Test Session Validation and Timeout Handling")
    void testSessionValidationAndTimeout() {
        // Setup expired session
        when(mockUserSession.getUsername()).thenReturn(STUDENT_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.STUDENT);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        when(mockUserSession.isExpired()).thenReturn(true);
        
        // Test access with expired session
        AccessResult result = accessController.checkAccess(mockUserSession, AccessPermission.VIEW_QUESTIONS);
        
        assertThat(result.isGranted())
            .as("Expired session should be denied access")
            .isFalse();
            
        assertThat(result.getDenialReason())
            .as("Denial reason should indicate session timeout")
            .containsIgnoringCase("session")
            .containsIgnoringCase("expired");
    }
    
    /**
     * Tests role hierarchy and permission inheritance.
     * 
     * <p>This test validates that role hierarchies are properly implemented
     * where higher-level roles inherit permissions from lower-level roles.
     * For example, admins should have all reviewer permissions plus additional
     * administrative capabilities.</p>
     * 
     * @testCase Verify role hierarchy and permission inheritance
     * @precondition Access controller implements role hierarchy
     * @postcondition Higher roles inherit lower role permissions
     * @expectedResult Permission inheritance works correctly across role hierarchy
     */
    @Test
    @DisplayName("Test Role Hierarchy and Permission Inheritance")
    void testRoleHierarchyAndInheritance() {
        // Test that admin inherits all reviewer permissions
        when(mockUserSession.getUsername()).thenReturn(ADMIN_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.ADMIN);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Admin should have all reviewer permissions
        AccessResult editResult = accessController.checkAccess(mockUserSession, AccessPermission.EDIT_QUESTIONS);
        AccessResult approveResult = accessController.checkAccess(mockUserSession, AccessPermission.APPROVE_CONTENT);
        
        assertThat(editResult.isGranted())
            .as("Admin should inherit reviewer's edit permission")
            .isTrue();
            
        assertThat(approveResult.isGranted())
            .as("Admin should inherit reviewer's approval permission")
            .isTrue();
            
        // Admin should also have exclusive admin permissions
        AccessResult manageResult = accessController.checkAccess(mockUserSession, AccessPermission.MANAGE_USERS);
        assertThat(manageResult.isGranted())
            .as("Admin should have exclusive admin permissions")
            .isTrue();
    }
    
    /**
     * Tests concurrent access control evaluation.
     * 
     * <p>This test validates that the access control system handles concurrent
     * permission checks safely without race conditions. Multiple simultaneous
     * access evaluations should be processed independently and correctly.</p>
     * 
     * @testCase Verify thread safety for concurrent access control evaluation
     * @precondition Access controller supports concurrent access
     * @postcondition Concurrent access checks are handled safely without conflicts
     * @expectedResult All concurrent access evaluations are processed correctly
     */
    @Test
    @DisplayName("Test Concurrent Access Control Evaluation")
    void testConcurrentAccessControlEvaluation() throws InterruptedException {
        // Setup authenticated user session
        when(mockUserSession.getUsername()).thenReturn(STUDENT_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.STUDENT);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Create multiple access check threads
        Thread[] threads = new Thread[10];
        AccessResult[] results = new AccessResult[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = accessController.checkAccess(mockUserSession, AccessPermission.VIEW_QUESTIONS);
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for completion
        for (Thread thread : threads) {
            thread.join(5000); // 5 second timeout
        }
        
        // Verify all access checks completed correctly
        for (int i = 0; i < 10; i++) {
            assertThat(results[i])
                .as("Concurrent access check %d should not be null", i)
                .isNotNull();
                
            assertThat(results[i].isGranted())
                .as("Concurrent access check %d should succeed for student viewing questions", i)
                .isTrue();
        }
    }
    
    /**
     * Tests access control performance characteristics.
     * 
     * <p>This test ensures that access control evaluation completes within
     * reasonable time limits to avoid impacting user experience. Permission
     * checks should be fast enough for interactive applications.</p>
     * 
     * @testCase Verify acceptable performance for access control evaluation
     * @precondition Access controller is optimized for performance
     * @postcondition Access control checks complete within time limits
     * @expectedResult Permission evaluation completes in less than 50ms
     */
    @Test
    @DisplayName("Test Access Control Performance")
    void testAccessControlPerformance() {
        // Setup authenticated user session
        when(mockUserSession.getUsername()).thenReturn(STUDENT_USERNAME);
        when(mockUserSession.getUserRole()).thenReturn(UserRole.STUDENT);
        when(mockUserSession.isAuthenticated()).thenReturn(true);
        
        // Measure access control time
        long startTime = System.currentTimeMillis();
        AccessResult result = accessController.checkAccess(mockUserSession, AccessPermission.VIEW_QUESTIONS);
        long endTime = System.currentTimeMillis();
        
        long executionTime = endTime - startTime;
        
        // Verify performance
        assertThat(result)
            .as("Performance test should return valid result")
            .isNotNull();
            
        assertThat(executionTime)
            .as("Access control evaluation should complete within 50ms")
            .isLessThan(50);
    }
}