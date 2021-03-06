package github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.ServiceResponse;
import com.github.joerodriguez.sbng2ex.TestDataSource;
import com.github.joerodriguez.sbng2ex.User;
import com.github.joerodriguez.sbng2ex.UsersRepository;
import com.github.joerodriguez.sbng2ex.invitation.EmailService;
import com.github.joerodriguez.sbng2ex.invitation.InvitationRequest;
import com.github.joerodriguez.sbng2ex.invitation.InvitationService;
import com.github.joerodriguez.sbng2ex.invitation.PasswordGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class InvitationServiceTest {

    @Mock
    EmailService emailService;

    @Mock
    UsersRepository usersRepository;

    @Mock
    PasswordGenerator passwordGenerator;

    InvitationService invitationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        invitationService = new InvitationService(
                passwordGenerator,
                emailService,
                usersRepository
        );
    }

    @Test
    public void testUserIsCreatedAndNotified() {
        doReturn("funkyFresh").when(passwordGenerator).get();
        doReturn(new User(1, "testuser@example.com")).when(usersRepository).create("testuser@example.com", "funkyFresh");


        ServiceResponse<User> response = invitationService.invite(new InvitationRequest("testuser@example.com"));


        assertThat(response.isSuccess(), equalTo(true));
        verify(emailService).sendInvitation(eq("testuser@example.com"), eq("funkyFresh"));
    }
}