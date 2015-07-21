package info.hb.lookup.object.common;

import java.util.List;

/**
 * Same as ImageBinaryChannel but with addition of Integral Image over Zero Mean
 * Image matrix.
 */
public class ImageBinaryChannelFeature extends ImageBinaryChannel {

	public List<FeatureK> k;

	public IntegralImage zeroMeanIntegral;

	public ImageBinaryChannelFeature(ChannelType t) {
		super(t);
		integral = new IntegralImage();
		integral2 = new IntegralImage2();
	}

	public ImageBinaryChannelFeature(ChannelType t, SArray template, FeatureSet list) {
		super(t, template);

		zeroMeanIntegral = new IntegralImage(zeroMean);

		init(list);
	}

	public ImageBinaryChannelFeature(ChannelType t, SArray template, double threshold) {
		super(t, template);

		zeroMeanIntegral = new IntegralImage(zeroMean);

		FeatureSet list = new FeatureSetAuto(this, threshold);

		init(list);
	}

	void init(FeatureSet list) {
		k = list.k(zeroMeanIntegral);
	}

}