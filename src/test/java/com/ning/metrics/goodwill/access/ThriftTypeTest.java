/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.goodwill.access;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThriftTypeTest
{
    private static final String THRIFT_TYPE_NAME = "FrontDoorVisit";
    private static final String THRIFT_FIELD_NAME = "fileName";
    private static final String THRIFT_FIELD_TYPE = "string";
    private static final Integer THRIFT_FIELD_POSITION = 1;
    private static final String THRIFT_FIELD_DESCRIPTION = "Name of the file attached to a blob";
    private static final String THRIFT_FIELD_SQL_TYPE = "varchar";
    private static final Integer THRIFT_FIELD_SQL_LENGTH = 255;
    private static final Integer THRIFT_FIELD_SQL_SCALE = 12;
    private static final Integer THRIFT_FIELD_SQL_PRECISION = 4;

    private ThriftField thriftFieldWithSQLAndDescription;
    private ThriftType thriftType;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeTest(alwaysRun = true)
    public void setUp()
    {
        thriftFieldWithSQLAndDescription = new ThriftField(THRIFT_FIELD_NAME, THRIFT_FIELD_TYPE, THRIFT_FIELD_POSITION,
            THRIFT_FIELD_DESCRIPTION, THRIFT_FIELD_SQL_TYPE, THRIFT_FIELD_SQL_LENGTH, THRIFT_FIELD_SQL_SCALE, THRIFT_FIELD_SQL_PRECISION);

        ArrayList<ThriftField> fields = new ArrayList<ThriftField>();
        fields.add(thriftFieldWithSQLAndDescription);
        thriftType = new ThriftType(THRIFT_TYPE_NAME, fields);
    }

    @Test
    public void testFindByName() throws Exception
    {
        ThriftField fieldNull = thriftType.getFieldByName("Idontexist");
        Assert.assertNull(fieldNull);

        ThriftField field = thriftType.getFieldByName(THRIFT_FIELD_NAME);
        Assert.assertNotNull(field);
        Assert.assertEquals(field.getName(), THRIFT_FIELD_NAME);
        Assert.assertEquals(field.getType(), THRIFT_FIELD_TYPE);
        Assert.assertEquals(field.getPosition(), THRIFT_FIELD_POSITION);
        Assert.assertEquals(field.getDescription(), THRIFT_FIELD_DESCRIPTION);
        Assert.assertEquals(field.getSql().getType(), THRIFT_FIELD_SQL_TYPE);
        Assert.assertEquals(field.getSql().getLength(), THRIFT_FIELD_SQL_LENGTH);
        Assert.assertEquals(field.getSql().getScale(), THRIFT_FIELD_SQL_SCALE);
        Assert.assertEquals(field.getSql().getPrecision(), THRIFT_FIELD_SQL_PRECISION);
        Assert.assertEquals(field, thriftFieldWithSQLAndDescription);
    }

    @Test
    public void testToJson() throws Exception
    {
        thriftType = ThriftType.decode(thriftType.toJSON().toString());
        runAllAssertions(thriftType);

        ThriftType type = mapper.readValue(new ByteArrayInputStream(thriftType.toJSON().toByteArray()), ThriftType.class);
        runAllAssertions(type);
    }

    private void runAllAssertions(ThriftType type) throws IOException
    {
        Assert.assertEquals(type.getName(), THRIFT_TYPE_NAME);
        Assert.assertEquals(type.getFieldByPosition(THRIFT_FIELD_POSITION).toString(), thriftFieldWithSQLAndDescription.toString());
    }
}
