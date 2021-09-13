package org.example.slee.sbb.local;

import net.java.slee.resource.sip.DialogActivity;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbLocalObject;
import java.io.Serializable;

public interface IncomingParticipantLocalInterface extends SbbLocalObject {
    public void init(RequestEvent event, ActivityContextInterface aci, SbbLocalObject parent);
    public void sendResponseToLeg1(ResponseEvent event, ServerTransaction st);
}
