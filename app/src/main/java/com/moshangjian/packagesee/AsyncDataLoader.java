package com.moshangjian.packagesee;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class AsyncDataLoader<D> extends AsyncTaskLoader<D> {

	D mData;

	public AsyncDataLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(D data) {
		if (isReset()) {
			if (mData != null) {
				onReleaseResources(mData);
			}
		}
		D oldData = mData;
		mData = data;
		if (isStarted()) {
			super.deliverResult(mData);
		}
		if (oldData != null) {
			onReleaseResources(oldData);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mData != null) {
			deliverResult(mData);
		}
		if (takeContentChanged() || mData == null) {
			forceLoad();
		}
	}

	@Override
	public void onCanceled(D data) {
		super.onCanceled(data);
		onReleaseResources(data);
	}

	public D getResult() {
		return mData;
	}

	public void changeResult(D newResult) {
		mData = newResult;
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		onReleaseResources(mData);
		if (mData != null) {
			mData = null;
		}
	}

	@Override
	protected void onAbandon() {
		super.onAbandon();
		cancelLoad();
	}

	protected void onReleaseResources(D data) {
	}
}
