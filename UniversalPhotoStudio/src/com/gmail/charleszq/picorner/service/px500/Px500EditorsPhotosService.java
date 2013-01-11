/**
 * 
 */
package com.gmail.charleszq.picorner.service.px500;

import java.util.List;

import com.github.yuyang226.j500px.J500px;
import com.github.yuyang226.j500px.photos.GlobalFeatures;
import com.github.yuyang226.j500px.photos.ImageSize;
import com.github.yuyang226.j500px.photos.Photo;
import com.gmail.charleszq.picorner.model.MediaObjectCollection;
import com.gmail.charleszq.picorner.utils.ModelUtils;

/**
 * @author charles(charleszq@gmail.com)
 *
 */
public class Px500EditorsPhotosService extends AbstractPxPhotoListService {
	
	public Px500EditorsPhotosService() {
		super();
	}

	public Px500EditorsPhotosService(String token, String secret) {
		super(token, secret);
	}

	@Override
	public MediaObjectCollection getPhotos(int pageSize, int pageNo)
			throws Exception {
		J500px px = getJ500px();
		List<Photo> photos = px.getPhotosInterface().getPhotos(GlobalFeatures.EDITORS, 
				null, null, null, ImageSize.LARGEST, null, pageNo + 1, pageSize);
		return ModelUtils.convertPx500Photos(photos);
	}

}
