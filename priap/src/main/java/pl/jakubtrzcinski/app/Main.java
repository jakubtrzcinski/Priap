package pl.jakubtrzcinski.app;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.SimplePriapSession;

@RequiredArgsConstructor
class FooService  {

    private final SimplePriapSession session;

    public void foo() {
        session.rerunFromHospital();
    }
}
