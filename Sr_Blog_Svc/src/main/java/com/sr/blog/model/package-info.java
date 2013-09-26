@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type=DateTime.class,
        value=DateTimeAdapter.class),
})
package com.sr.blog.model;
 
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.joda.time.DateTime;
import com.sr.blog.util.DateTimeAdapter;
