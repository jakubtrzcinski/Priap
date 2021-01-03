package pl.jakubtrzcinski.app;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.PriapSession;

@RequiredArgsConstructor
class FooService  {

    private final PriapSession session;

    public void foo() {
        session.rerunFromHospital();
    }
}
