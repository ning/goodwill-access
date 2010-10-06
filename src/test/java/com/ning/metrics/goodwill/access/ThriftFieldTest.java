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

public class ThriftFieldTest
{
    private static final String THRIFT_FIELD_NAME = "fileName";
    private static final String THRIFT_FIELD_TYPE = "string";
    private static final Integer THRIFT_FIELD_POSITION = 1;
    private static final String THRIFT_FIELD_DESCRIPTION = "Name of the file attached to a blob";
    private static final String THRIFT_FIELD_SQL_TYPE = "varchar";
    private static final Integer THRIFT_FIELD_SQL_LENGTH = 255;
    private static final Integer THRIFT_FIELD_SQL_SCALE = 12;
    private static final Integer THRIFT_FIELD_SQL_PRECISION = 12;

    private ThriftField thriftField;
    private ThriftField thriftFieldWithSQL;
    private ThriftField thriftFieldWithDescription;
    private ThriftField thriftFieldWithSQLAndDescription;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeTest(alwaysRun = true)
    public void setUp()
    {
        thriftField = new ThriftField(THRIFT_FIELD_NAME, THRIFT_FIELD_TYPE, THRIFT_FIELD_POSITION, null, null, null, null, null);
        thriftFieldWithSQL = new ThriftField(THRIFT_FIELD_NAME, THRIFT_FIELD_TYPE, THRIFT_FIELD_POSITION,
            null, THRIFT_FIELD_SQL_TYPE, THRIFT_FIELD_SQL_LENGTH, null, null);
        thriftFieldWithDescription = new ThriftField(THRIFT_FIELD_NAME, THRIFT_FIELD_TYPE, THRIFT_FIELD_POSITION,
            THRIFT_FIELD_DESCRIPTION, null, null, null, null);
        thriftFieldWithSQLAndDescription = new ThriftField(THRIFT_FIELD_NAME, THRIFT_FIELD_TYPE, THRIFT_FIELD_POSITION,
            THRIFT_FIELD_DESCRIPTION, THRIFT_FIELD_SQL_TYPE, THRIFT_FIELD_SQL_LENGTH, THRIFT_FIELD_SQL_SCALE, THRIFT_FIELD_SQL_PRECISION);
    }

    @Test
    public void testJSONConstructor() throws Exception
    {
        thriftField = ThriftField.decode(thriftField.toJSON().toString());
        thriftFieldWithSQL = ThriftField.decode(thriftFieldWithSQL.toJSON().toString());
        thriftFieldWithDescription = ThriftField.decode(thriftFieldWithDescription.toJSON().toString());
        thriftFieldWithSQLAndDescription = ThriftField.decode(thriftFieldWithSQLAndDescription.toJSON().toString());

        runAllAsserts();
    }

    @Test
    public void testToJSON() throws Exception
    {
        runAllAsserts();
    }

    private void runAllAsserts() throws IOException
    {
        ThriftField field;
        ThriftField.Sql jsonThriftFieldSQL;

        field = mapper.readValue(new ByteArrayInputStream(thriftField.toJSON().toByteArray()), ThriftField.class);
        Assert.assertEquals(field.getName(), THRIFT_FIELD_NAME);
        Assert.assertEquals(field.getType(), THRIFT_FIELD_TYPE);
        Assert.assertEquals((int) field.getPosition(), (int) THRIFT_FIELD_POSITION);

        field = mapper.readValue(new ByteArrayInputStream(thriftFieldWithSQL.toJSON().toByteArray()), ThriftField.class);
        Assert.assertEquals(field.getName(), THRIFT_FIELD_NAME);
        Assert.assertEquals(field.getType(), THRIFT_FIELD_TYPE);
        Assert.assertEquals((int) field.getPosition(), (int) THRIFT_FIELD_POSITION);
        jsonThriftFieldSQL = field.getSql();
        Assert.assertEquals(jsonThriftFieldSQL.getType(), THRIFT_FIELD_SQL_TYPE);
        Assert.assertEquals((int) jsonThriftFieldSQL.getLength(), (int) THRIFT_FIELD_SQL_LENGTH);

        field = mapper.readValue(new ByteArrayInputStream(thriftFieldWithDescription.toJSON().toByteArray()), ThriftField.class);
        Assert.assertEquals(field.getName(), THRIFT_FIELD_NAME);
        Assert.assertEquals(field.getType(), THRIFT_FIELD_TYPE);
        Assert.assertEquals((int) field.getPosition(), (int) THRIFT_FIELD_POSITION);
        Assert.assertEquals(field.getDescription(), THRIFT_FIELD_DESCRIPTION);

        field = mapper.readValue(new ByteArrayInputStream(thriftFieldWithSQLAndDescription.toJSON().toByteArray()), ThriftField.class);
        Assert.assertEquals(field.getName(), THRIFT_FIELD_NAME);
        Assert.assertEquals(field.getType(), THRIFT_FIELD_TYPE);
        Assert.assertEquals((int) field.getPosition(), (int) THRIFT_FIELD_POSITION);
        Assert.assertEquals(field.getDescription(), THRIFT_FIELD_DESCRIPTION);
        jsonThriftFieldSQL = field.getSql();
        Assert.assertEquals(jsonThriftFieldSQL.getType(), THRIFT_FIELD_SQL_TYPE);
        Assert.assertEquals((int) jsonThriftFieldSQL.getLength(), (int) THRIFT_FIELD_SQL_LENGTH);
    }
}
