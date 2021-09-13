package org.example.slee.sbb.rootsbb;

import com.opencloud.javax.sip.PersistentOutboundConnection;
import jdk.nashorn.internal.ir.RuntimeNode;
import net.java.slee.resource.sip.DialogActivity;
import org.example.slee.sbb.local.IncomingParticipantLocalInterface;
import org.example.slee.sbb.local.RootSbbLocalInterface;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.header.Header;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRolledbackLocalException;

public abstract class IncomingParticipantChildSbb extends BaseSbb implements IncomingParticipantLocalInterface {
    @Override
    public void setSbbContext(SbbContext context) {
        super.setSbbContext(context);
    }

    public void init(RequestEvent event, ActivityContextInterface incomingDailogACI,SbbLocalObject parent) {
        tracer.info("received invite from ParentSBB"+event.getRequest());
        incomingDailogACI.attach(getSbblocalObject());
        setParent((RootSbbLocalInterface) parent);
        DialogActivity incomingDialog;
        setIncomingParticipantACI(incomingDailogACI);

    }

    public void sendResponseToLeg1(ResponseEvent event,ServerTransaction st ) {
            sendResponse(st,event);
    }

    private void sendResponse(ServerTransaction st, ResponseEvent event) {
        try {
            tracer.info("got response from Parent SBB"+event.getResponse());
            Response response = getSipMessageFactory().createResponse(event.getResponse().getStatusCode(), st.getRequest());
            ToHeader toHeader= (ToHeader) response.getHeader(ToHeader.NAME);
            ToHeader incomingToHeader= (ToHeader) event.getResponse().getHeader(ToHeader.NAME);
            toHeader.setTag(incomingToHeader.getTag());
            st.sendResponse(response);
            tracer.info("200 OK Response sent successfully to UAC");

        } catch (Exception e) {
            tracer.severe("failed to send error response", e);
        }
    }

    public void onAck(RequestEvent event, ActivityContextInterface aci) {
        tracer.info("got ACK from UAC");
        getParent().forwardRequestToLeg2(event);
    }

    public void onBye(RequestEvent event, ActivityContextInterface aci) {
        tracer.info("received Bye request from uac   "+event.getRequest());
        try {
            Response response = getSipMessageFactory().createResponse(200,event.getRequest());
            event.getServerTransaction().sendResponse(response);
            tracer.info("sending 200 to UAC successfully  "+response);
        } catch (Exception e) {
            tracer.info("unable to send response"+e);
        }
        getIncomingParticipantACI().detach(getSbblocalObject());
        setIncomingParticipantACI(null);
        getParent().forwardRequestToLeg2(event);
    }


    public abstract RootSbbLocalInterface getParent();
    public abstract void setParent(RootSbbLocalInterface parent);

    public abstract ActivityContextInterface getIncomingParticipantACI();
    public abstract void setIncomingParticipantACI(ActivityContextInterface incomingParticipantACI);


}

