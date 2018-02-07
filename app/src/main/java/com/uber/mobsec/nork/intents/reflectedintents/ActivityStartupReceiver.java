package com.uber.mobsec.nork.intents.reflectedintents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * This BroadcastReciever will take in an intent with externally controllable params that has
 * the next intent to run.  E.g. way to exploit this would be to have some app do:
 *
 * Intent intentToRelaunch = new Intent(context, InternalActivity.class);
 * intentToRelaunch.setClassName("com.uber.mobsec.nork", "com.uber.mobsec.nork.intents.reflectedintents.InternalActivity");
 * intent.putExtra(BR_ACTIVITY_INTENT_EXTRA, intentToRelaunch);
 *
 */
public class ActivityStartupReceiver extends BroadcastReceiver {
    private static String BR_ACTIVITY_INTENT_EXTRA = "br_activity_intent_extra";
    public ActivityStartupReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = getActivityIntent(context, intent);
        if (i != null)
            context.startActivity(i);
    }

    /**
     * Starts next activity and returns started {@link Intent}.
     *
     * @return Started Intent.
     */
    @Nullable
    private Intent getActivityIntent(Context context, Intent intent) {
        if (intent.hasExtra(BR_ACTIVITY_INTENT_EXTRA)) {
            return intent.getParcelableExtra(BR_ACTIVITY_INTENT_EXTRA);
        }
        return null;
    }
}
