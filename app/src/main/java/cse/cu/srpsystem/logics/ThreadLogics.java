package cse.cu.srpsystem.logics;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class ThreadLogics {

    public static class CustomThread extends AsyncTask<Object, Object, StatusListener.Status> {
        private StatusListener listener;
        private Runnable runnable;
        private Object[] parameters;

        public CustomThread(@NonNull Runnable runnable) {
            this(runnable, null);
        }

        public CustomThread(@NonNull Runnable runnable, StatusListener listener) {
            this.runnable = runnable;
            this.listener = listener;
        }

        @Override
        protected StatusListener.Status doInBackground(Object... parameters) {
            this.parameters = parameters;
            return this.runnable.run(parameters);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(StatusListener.Status status) {
            if (listener != null) listener.listen(status);
        }

        public interface Runnable {
            StatusListener.Status run(Object... parameters);
        }
    }
}
