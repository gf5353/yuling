package org.mobile.gqz.db.converter;


import org.mobile.gqz.db.sqlite.ColumnDbType;

import android.database.Cursor;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class ByteArrayColumnConverter implements ColumnConverter<byte[]> {
    @Override
    public byte[] getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getBlob(index);
    }

    @Override
    public byte[] getFieldValue(String fieldStringValue) {
        return null;
    }

    @Override
    public Object fieldValue2ColumnValue(byte[] fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.BLOB;
    }
}
