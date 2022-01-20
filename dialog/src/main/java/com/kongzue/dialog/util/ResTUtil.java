/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kongzue.dialog.util;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.global.resource.*;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.util.Optional;

public class ResTUtil {
    private static final String TAG = ResTUtil.class.getSimpleName();

    private static final String CITY_ID_ATTR = "cityId_";

    private static final String STRING_ID_ATTR = "String_";

    private ResTUtil() {
    }

    /**
     * get the path from id
     *
     * @param context the context
     * @param id      the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {
        String path = "";
        if (context == null) {
            Log.e(TAG, "getPathById -> get null context");
            return path;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            Log.e(TAG, "getPathById -> get null ResourceManager");
            return path;
        }
        try {
            path = manager.getMediaPath(id);
        } catch (IOException e) {
            Log.e(TAG, "getPathById -> IOException");
        } catch (NotExistException e) {
            Log.e(TAG, "getPathById -> NotExistException");
        } catch (WrongTypeException e) {
            Log.e(TAG, "getPathById -> WrongTypeException");
        }
        return path;
    }

    public static ShapeElement getLineShape(Context context, int width, int color) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.LINE);
        shapeElement.setStroke(width, RgbColor.fromArgbInt(context.getColor(color)));
        return shapeElement;
    }

    public static final ShapeElement getShapeRectElement(int color, int getRight, int getBottom) {
        ShapeElement shape_element = new ShapeElement();
        shape_element.setShape(ShapeElement.RECTANGLE);
        shape_element.setRgbColor(RgbColor.fromArgbInt(color));
        shape_element.setBounds(0, 0, getRight, getBottom);
        shape_element.setCornerRadius(1.0f);
        return shape_element;
    }

    /**
     * get the color
     *
     * @param context the context
     * @param id      the id
     * @return the color
     */
    public static int getColor(Context context, int id) {
        int result = 0;
        if (context == null) {
            Log.e(TAG, "getColor -> get null context");
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            Log.e(TAG, "getColor -> get null ResourceManager");
            return result;
        }
        try {
            result = manager.getElement(id).getColor();
        } catch (IOException e) {
            Log.e(TAG, "getColor -> IOException");
        } catch (NotExistException e) {
            Log.e(TAG, "getColor -> NotExistException");
        } catch (WrongTypeException e) {
            Log.e(TAG, "getColor -> WrongTypeException");
        }
        return result;
    }

    /**
     * get the pixel map
     *
     * @param context the context
     * @param id      the id
     * @return the pixel map
     */
    public static Optional<PixelMap> getPixelMap(Context context, int id) {
        String path = getPathById(context, id);
        if (TextUtils.isEmpty(path)) {
            Log.e(TAG, "getPixelMap -> get empty path");
            return Optional.empty();
        }
        RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
        ImageSource.SourceOptions options = new ImageSource.SourceOptions();
        options.formatHint = "image/png";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        try {
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions));
        } catch (IOException e) {
            Log.e(TAG, "getPixelMap -> IOException");
        }
        return Optional.empty();
    }

    /**
     * get the Pixel Map Element
     *
     * @param context the context
     * @param resId   the res id
     * @return the Pixel Map Element
     */
    public static PixelMapElement getPixelMapDrawable(Context context, int resId) {
        Optional<PixelMap> optional = getPixelMap(context, resId);
        return optional.map(PixelMapElement::new).orElse(null);
    }
}

