package com.jbull.hermes.android;

import com.jbull.hermes.*;
import junit.framework.TestCase;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Created by Jordan on 3/30/14.
 */
public class SendFavoredCommunicationSchedulerTest extends TestCase {

    private MessageSender sender;
    private MessageListener listener;
    private int sendWindow;
    private SendFavoredCommunicationScheduler commScheduler;
    private Message.SetupMessage msg;

    public void setUp() throws Exception {
        listener = mock(MessageListener.class);
        sender = mock(MessageSender.class);
        sendWindow = -1;
        commScheduler = new SendFavoredCommunicationScheduler(sender, listener, sendWindow);
        msg = MessageHelper.createSetupMessage();
    }

    public void testStart() throws Exception {
        // never listens
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                commScheduler.start();
            }
        });
        thread.start();
        while(thread.isAlive()) {
            commScheduler.stop();
        }
        thread.join();
        verify(listener, never()).listen();

        // send and then listen. stop listening if listener.listen returns non Mode.LISTENING
        commScheduler = new SendFavoredCommunicationScheduler(sender, listener, 5);
        commScheduler.send(msg);
        when(listener.listen()).thenReturn(Mode.STOPPED);
        commScheduler.start();
        InOrder inOrder = inOrder(sender, listener);
        inOrder.verify(sender, times(1)).send(msg);
        inOrder.verify(listener, times(1)).listen();

        //alternate send and listen
        listener = mock(MessageListener.class);
        sender = mock(MessageSender.class);
        commScheduler = new SendFavoredCommunicationScheduler(sender, listener, 5);
        commScheduler.send(msg);
        when(listener.listen()).thenReturn(Mode.SENDING).thenReturn(Mode.STOPPED);
        commScheduler.start();
        inOrder = inOrder(sender, listener);
        inOrder.verify(sender, times(1)).send(msg);
        inOrder.verify(listener, times(2)).listen();
    }

}