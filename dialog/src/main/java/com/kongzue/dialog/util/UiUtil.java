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
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.utils.ParcelableEx;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.utils.Parcel;
import ohos.utils.ParcelException;

import java.io.*;
import java.util.Locale;
import java.util.Optional;

public final class UiUtil {
    private static final String TAG = UiUtil.class.getSimpleName();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, UiUtil.class.getSimpleName());

    /**
     * get the color
     *
     * @param context the context
     * @param id      the id
     * @return the color
     */
    public static int getColor(Context context, int id) {
        int result = 0;
        if (context != null) {
            ResourceManager manager = context.getResourceManager();
            if (manager != null) {
                try {
                    result = manager.getElement(id).getColor();
                } catch (IOException | NotExistException | WrongTypeException e) {
                    HiLog.error(LABEL_LOG, String.format(Locale.ROOT, "%s", e.getMessage()));
                }
            }
        }
        return result;
    }

    public static final ShapeElement getShapeElement(int shape, int color, float radius) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(shape);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(color));
        shapeElement.setCornerRadius(radius);
        return shapeElement;
    }

    public static <E extends Component> E getComponent(Component root, int id) {
        if (root == null) {
            return null;
        }
        try {
            return (E) root.findComponentById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    public static PixelMap createByResourceId(Context context, int resourceId) {
        if (context == null) {
            return null;
        }

        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return null;
        }

        ohos.global.resource.Resource resource = null;
        try {
            resource = manager.getResource(resourceId);
        } catch (IOException | NotExistException e) {
            e.printStackTrace();
        }
        if (resource == null) {
            return null;
        }

        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        srcOpts.formatHint = "image/png";
        ImageSource imageSource = null;
        try {
            imageSource = ImageSource.create(readResource(resource), srcOpts);
        } finally {
            close(resource);
        }
        if (imageSource == null) {
            return null;
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(0, 0);
        decodingOpts.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;
        PixelMap pixelmap = imageSource.createPixelmap(decodingOpts);
        return pixelmap;
    }

    private static byte[] readResource(ohos.global.resource.Resource resource) {
        final int bufferSize = 1024;
        final int ioEnd = -1;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        while (true) {
            try {
                int readLen = resource.read(buffer, 0, bufferSize);
                if (readLen == ioEnd) {
                    break;
                }
                output.write(buffer, 0, readLen);
            } catch (IOException e) {
                break;
            }
        }
        return output.toByteArray();
    }

    private static void close(ohos.global.resource.Resource resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
            }
        }
    }

    public static Element getPixelElementByResId(Context context, int resourceId) {
        return new PixelMapElement(createByResourceId(context, resourceId)).getCurrentElement();
    }

    private static final String RAW_FILE_PATH = "entry/resources/rawfile/";
    private static final int BUFFER_LENGTH = 8192;
    public static final int DEFAULT_ERROR = -1;

    public static Font getFont(Context context, int fontResId, int style, boolean italic) {
        String fontFamily = context.getString(fontResId);
        String path = RAW_FILE_PATH + fontFamily;
        File file = new File(context.getDataDir(), fontFamily);
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = context.getResourceManager().getRawFileEntry(path).openRawFile()) {
            byte[] buffer = new byte[BUFFER_LENGTH];
            int bytesRead = inputStream.read(buffer, 0, BUFFER_LENGTH);
            while (bytesRead != DEFAULT_ERROR) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer, 0, BUFFER_LENGTH);
            }
        } catch (FileNotFoundException exception) {
            Log.e(TAG, "loadFontFromFile -> FileNotFoundException : " + exception.getLocalizedMessage());
        } catch (IOException exception) {
            Log.e(TAG, "loadFontFromFile -> IOException : " + exception.getLocalizedMessage());
        }
        return Optional.of(new Font.Builder(file).setWeight(style).makeItalic(italic).build()).get();
    }

    /**
     * This is called to find out how big a view should be. The parent supplies constraint information in the width and height parameters.
     *
     * @param widthMeasureSpec  Horizontal space requirements as imposed by the parent
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent
     */
    public static void measure(int widthMeasureSpec, int heightMeasureSpec) {
        Insets insets = new Insets(0, 0, 0, 0);
        int oWidth = insets.left + insets.right;
        int oHeight = insets.top + insets.bottom;
        widthMeasureSpec = widthMeasureSpec + oWidth;
        heightMeasureSpec = heightMeasureSpec + oHeight;
    }

    private static class Insets implements ParcelableEx {
        final int left;
        final int top;
        final int right;
        final int bottom;

        private Insets(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        public void marshallingEx(Parcel parcel) throws ParcelException {
            parcel.writeInt(left);
            parcel.writeInt(top);
            parcel.writeInt(right);
            parcel.writeInt(bottom);
        }
    }

}
