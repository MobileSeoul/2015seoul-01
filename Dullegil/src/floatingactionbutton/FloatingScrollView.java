package floatingactionbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class FloatingScrollView extends ScrollView {

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView mScrollView, int x, int y, int oldx,
				int oldy);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public FloatingScrollView(Context mContext) {
		super(mContext);
	}

	public FloatingScrollView(Context mContext, AttributeSet attrs) {
		super(mContext, attrs);
	}

	public FloatingScrollView(Context mContext, AttributeSet attrs, int defStyle) {
		super(mContext, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);

		if (mOnScrollChangedListener != null)
			mOnScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener mListener) {
		mOnScrollChangedListener = mListener;
	}

}