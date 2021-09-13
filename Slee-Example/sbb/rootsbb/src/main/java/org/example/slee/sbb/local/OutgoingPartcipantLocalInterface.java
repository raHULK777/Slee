package org.example.slee.sbb.local;

import net.java.slee.resource.sip.DialogActivity;

import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbLocalObject;

public interface OutgoingPartcipantLocalInterface extends SbbLocalObject {
    public void init(RequestEvent event,DialogActivity outgoingDialog, SbbLocalObject parent);
    public void sendRequestToLeg2(RequestEvent event);
}
