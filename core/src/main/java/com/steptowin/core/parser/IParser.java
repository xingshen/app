package com.steptowin.core.parser;


import java.io.InputStream;

/**
 *  {@link com.thoughtworks.xstream.XStream}默认使用xpp3类库，pull解析
 *  Xpp3 is a very fast XML pull-parser implementation. If you do not want to include these dependencies,
 *  you can use a standard JAXP DOM parser or since Java 6 the integrated StAX parser instead:
 *  PULL解析：
 *  在android系统中，很多资源文件中,很多都是xml格式，在android系统中解析这些xml的方式，是使用pul解析器进行解析的，
 *  它和sax解析一样（个人感觉要比sax简单点），也是采用事件驱动进行解析的，当pull解析器，开始解析之后，我们可以调用它的next（）方法，
 *  来获取下一个解析事件（就是开始文档，结束文档，开始标签，结束标签），当处于某个元素时可以调用XmlPullParser的getAttributte()方法来获取属性的值，
 *  也可调用它的nextText()获取本节点的值。
 *  DOM解析：
 *  在Dom解析的过程中，是先把dom全部文件读入到内存中，然后使用dom的api遍历所有数据，检索想要的数据，这种方式显然是一种比较消耗内存的方式，
 *  对于像手机这样的移动设备来讲，内存是非常有限的，所以对于比较大的XML文件,不推荐使用这种方式，
 *  但是Dom也有它的优点，它比较直观，在一些方面比SAX方式比较简单。在xml文档比较小的情况下也可以考虑使用dom方式。
 * .
 */
public interface IParser {
    <T> T parse(InputStream in,Class<T> clazz);
}
