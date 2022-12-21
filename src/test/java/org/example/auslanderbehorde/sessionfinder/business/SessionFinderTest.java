package org.example.auslanderbehorde.sessionfinder.business;

import org.example.auslanderbehorde.sessionfinder.model.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class SessionFinderTest {

    @RepeatedTest(30)
    void findRequestId() throws InterruptedException {
        // GIVEN
        SessionFinder underTest = new SessionFinder();

        // WHEN
        Session session = underTest.findAndGetSession();

        // THEN
        Assertions.assertNotNull(session.getRequestId());
        Assertions.assertNotNull(session.getDsrid());
        Assertions.assertNotNull(session.getDswid());
    }

    // missgin test for a scenerioa what the netty timeout error is recevied
}