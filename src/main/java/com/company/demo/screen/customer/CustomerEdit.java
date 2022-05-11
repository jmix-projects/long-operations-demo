package com.company.demo.screen.customer;

import com.company.demo.screen.wait.WaitScreen;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Timer;
import io.jmix.ui.screen.*;
import com.company.demo.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@UiController("Customer.edit")
@UiDescriptor("customer-edit.xml")
@EditedEntityContainer("customerDc")
public class CustomerEdit extends StandardEditor<Customer> {

    private static final Logger log = LoggerFactory.getLogger(CustomerEdit.class);

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Timer timer;

    @Autowired
    private ScreenBuilders screenBuilders;

    private WaitScreen waitScreen;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        waitScreen = screenBuilders.screen(this)
                .withScreenClass(WaitScreen.class)
                .show();
        event.preventCommit();

        timer.start();
        timer.addTimerActionListener(timerActionEvent ->
                event.resume());
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> commitDelegate(SaveContext saveContext) {
        Set<Object> objects = longCommitOperation(saveContext);
        waitScreen.closeWithDefaultAction();
        return objects;
    }

    private Set<Object> longCommitOperation(SaveContext saveContext) {
        for (int i = 0; i < 5; i++) {
            log.info("doing {}...", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return dataManager.save(saveContext);
    }
}