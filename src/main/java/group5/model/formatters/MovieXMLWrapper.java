 package group5.model.formatters;

 import java.util.Collection;
 import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
 import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
 import group5.model.beans.MBeans;

 /**
  * This wrapper helps when using Jackson to serialize a list of domain records to xml. Without this,
  * it tries to use <ArrayList> and <item> tags instead of <domainList> and <domain> tags.
  *
  * Suggested use (note you need try/catch with this)
  *
  * <pre>
  * XmlMapper mapper = new XmlMapper();
  * mapper.enable(SerializationFeature.INDENT_OUTPUT);
  * DomainXmlWrapper wrapper = new DomainXmlWrapper(records);
  * mapper.writeValue(out, wrapper);
  * </pre>
  */
 @JacksonXmlRootElement(localName = "movieList")
 public final class MovieXMLWrapper {

     /** List of the records. */
     @JacksonXmlElementWrapper(useWrapping = false)
     private Collection<MBeans> movie;

     /**
      * Constructor.
      *
      * @param movies the movies to wrap
      */
     public MovieXMLWrapper(Collection<MBeans> movies) {
         this.movie = movies;
     }
 }
