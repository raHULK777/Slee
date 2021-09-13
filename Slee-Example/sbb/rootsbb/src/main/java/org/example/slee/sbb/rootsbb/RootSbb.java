package org.example.slee.sbb.rootsbb;

import com.opencloud.javax.sip.RequestPendingException;
import com.opencloud.javax.sip.slee.OCSipActivityContextInterfaceFactory;
import com.opencloud.javax.sip.slee.OCSleeSipProvider;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SleeSipProvider;
import org.example.slee.sbb.local.IncomingParticipantLocalInterface;
import org.example.slee.sbb.local.OutgoingPartcipantLocalInterface;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;
import javax.slee.*;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;
import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class RootSbb implements Sbb {
    private static final String CUSTOM_CONVERGENCE_NAME = "startservicesbb";
    private Tracer tracer;
    private OCSleeSipProvider sipSbbInterface;
    private OCSipActivityContextInterfaceFactory sipACIFactory;
    private SbbContext sbbContext;

    public void setSbbContext(SbbContext context) {
        tracer = context.getTracer(context.getSbb().getName());
        DataSource ds;
        try {
            sbbContext=context;
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            String providerName ="java:comp/env/slee/resources/sipra/provider";
            String factoryName ="java:comp/env/slee/resources/sipra/acifactory";
            sipSbbInterface = (OCSleeSipProvider) myEnv.lookup(providerName);
            sipACIFactory = (OCSipActivityContextInterfaceFactory) myEnv.lookup(factoryName);
        }
        catch (NamingException e) {
             tracer.severe("Could not set SBB context", e);
        }
    }

    public SbbLocalObject getSbblocalObject() {
        return this.sbbContext.getSbbLocalObject();
    }

    public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
        tracer.info("service started");
    }

    public InitialEventSelector serviceStartEventSelector(InitialEventSelector ies) {
        ies.setCustomName(CUSTOM_CONVERGENCE_NAME);
        return ies;
    }
    
    public void onInitialInvite(RequestEvent event, ActivityContextInterface aci) {
        tracer.info("received initial invite ");
        ServerTransaction st = event.getServerTransaction();
        aci.attach(getSbblocalObject());
        IncomingParticipantLocalInterface incChild;
        OutgoingPartcipantLocalInterface outChild;
        ActivityContextInterface incomingDialogACI = null;
        ActivityContextInterface outgoingDialogACI = null;
        DialogActivity outgoingDialog;
        DialogActivity incomingDialog = null;

        try {
            incChild = (IncomingParticipantLocalInterface) getIncomingParticipantChild().create();
            tracer.info("child SBB1 is created");
            incomingDialog = (DialogActivity) getSleeSipProvider().getNewDialog(event.getServerTransaction());
            incomingDialogACI=getSipACIFactory().getActivityContextInterface(incomingDialog);
            incChild.init(event, incomingDialogACI, getSbblocalObject());
            setIncomingLeg(incChild);
        } catch (Exception e) {
            tracer.info("Failed to create child SBB", e);
        }
        try {
            outChild = (OutgoingPartcipantLocalInterface) getOutgoingParticipantChild().create();
            tracer.info("child SBB2 is created");
            outgoingDialog= getSleeSipProvider().getNewDialog(incomingDialog,true);
            outChild.init(event,outgoingDialog, getSbblocalObject());
            setOutgoingLeg(outChild);
        } catch (Exception e) {
            tracer.info("Failed to create child SBB ", e);
        }




    }

    public void onAck(RequestEvent event, ActivityContextInterface aci) {
        tracer.info("received the acknowledgement "+event.getRequest().toString());
    }

    public void onBye(RequestEvent event, ActivityContextInterface aci) {
        ServerTransaction st=event.getServerTransaction();
        tracer.info("received bye"+event.getRequest().toString());
        sendResponse(st,Response.OK);
        aci.detach(getSbblocalObject());
    }

    public void forwardResponseToLeg1(ResponseEvent event,ServerTransaction st) {
        getIncomingLeg().sendResponseToLeg1(event,st);
    }

    public void forwardRequestToLeg2(RequestEvent event) {
        getOutgoingLeg().sendRequestToLeg2(event);
    }

     private void sendResponse(ServerTransaction st, int statusCode) {
        try {
            Response response = getSipMessageFactory().createResponse(statusCode, st.getRequest());
            ToHeader header=(ToHeader) response.getHeader(ToHeader.NAME);
            header.setTag("tag789");
            response.setHeader(header);
            tracer.info("response sent"+response.toString());
            st.sendResponse(response);
        } catch (Exception e) {
            tracer.severe("failed to send response", e);
        }
    }

    public void unsetSbbContext() {

    }

    public void sbbCreate() throws CreateException {

    }

    public void sbbPostCreate() throws CreateException {

    }

    public void sbbActivate() {

    }

    public void sbbPassivate() {

    }

    public void sbbLoad() {

    }

    public void sbbStore() {

    }

    public void sbbRemove() {

    }

    public void sbbExceptionThrown(Exception e, Object o, ActivityContextInterface activityContextInterface) {

    }

    public void sbbRolledBack(RolledBackContext rolledBackContext) {

    }

    public abstract String getContactHeader();
    public abstract void setContactHeader(String contactHeader);

    protected final OCSleeSipProvider getSleeSipProvider() { return sipSbbInterface; }
    protected final OCSipActivityContextInterfaceFactory getSipACIFactory() { return sipACIFactory; }
    protected final SipProvider getSipProvider() { return sipSbbInterface; }
    protected final AddressFactory getSipAddressFactory() {
        return sipSbbInterface.getAddressFactory();
    }
    protected final HeaderFactory getSipHeaderFactory() {
        return sipSbbInterface.getHeaderFactory();

    }
    protected final MessageFactory getSipMessageFactory() {
        return sipSbbInterface.getMessageFactory();
    }

    public abstract ChildRelation getIncomingParticipantChild();
    public abstract ChildRelation getOutgoingParticipantChild();

    public abstract IncomingParticipantLocalInterface getIncomingLeg();
    public abstract void setIncomingLeg(IncomingParticipantLocalInterface incChild);

    public abstract OutgoingPartcipantLocalInterface getOutgoingLeg();
    public abstract void setOutgoingLeg(OutgoingPartcipantLocalInterface outChild);
}
