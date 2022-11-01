package com.iscas.pm.api.model.doc.data;

import com.deepoove.poi.data.PictureRenderData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by  lichang
 * @date 2022/11/1.
 */
@Data
@Accessors(chain = true)
public class PoitlPicture {
   @ApiModelProperty(value = "图片输入流")
   private PictureRenderData streamImg;


   @ApiModelProperty(value = "图片名称")
   private String pictureName;
}
