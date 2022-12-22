package org.example.auslanderbehorde.sessionfinder.business;

import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class SessionInfoFinderTest {

    @RepeatedTest(30)
    void findRequestId() throws InterruptedException {
        // GIVEN
        SessionFinder underTest = new SessionFinder();

        // WHEN
        SessionInfo sessionInfo = underTest.findAndGetSession();

        // THEN
        Assertions.assertNotNull(sessionInfo.getRequestId());
        Assertions.assertNotNull(sessionInfo.getDsrid());
        Assertions.assertNotNull(sessionInfo.getDswid());
    }

    // missgin test for a scenerioa what the netty timeout error is recevied
}