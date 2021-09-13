package org.example.slee.sbb.local;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.slee.SbbLocalObject;

public interface RootSbbLocalInterface extends SbbLocalObject {
    public void forwardResponseToLeg1(ResponseEvent event, ServerTransaction st);
    public void forwardRequestToLeg2(RequestEvent event);
}
