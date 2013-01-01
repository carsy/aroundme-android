package pt.up.fe.aroundme.connections;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import pt.up.fe.aroundme.controllers.AroundMeController;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadJSONTask extends AsyncTask<URI, Integer, String> {
	protected AroundMeController aroundMeController;

	public DownloadJSONTask(final AroundMeController aroundMeController) {
		this.aroundMeController = aroundMeController;
	}

	@Override
	protected String doInBackground(final URI... uris) {
		final HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;

		try {
			response = httpclient.execute(new HttpGet(uris[0]));
			final StatusLine statusLine = response.getStatusLine();
			if( statusLine.getStatusCode() == HttpStatus.SC_OK ) {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			}
			else {
				response.getEntity().getContent().close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			Log.e(this.toString(), "Download error. Aborting.", e);
		}

		return responseString;
	}
}
