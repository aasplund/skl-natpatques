package se.callistaenterprise.rest.writers;

import static se.callistaenterprise.Utils.NEW_LINE;
import static se.callistaenterprise.Utils.QUOT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces("application/csv")
public class UriList2CSVWriter implements MessageBodyWriter<List<URI>> {

    private static final char SEPARATOR = ',';


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // Ensure that we're handling only Collection<HsaObject> objects.
        boolean isWritable;
        if (List.class.isAssignableFrom(type) && genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
            isWritable = (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(URI.class));
        } else {
            isWritable = false;
        }
        return isWritable;
    }

    @Override
    public long getSize(List<URI> t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(List<URI> uris, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {

        PrintStream ps = new PrintStream(out, true, "UTF-8");
        for (int i = 0; i < uris.size(); i++) {
            ps.append(QUOT).append(uris.get(i).toString()).append(QUOT);
            if(i < uris.size()-1) {
                ps.append(SEPARATOR);
            } else {
                ps.append(NEW_LINE);
            }
        }
        ps.close();
    }
    
    
}
