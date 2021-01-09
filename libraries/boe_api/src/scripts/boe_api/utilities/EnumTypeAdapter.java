package scripts.boe_api.utilities;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnumTypeAdapter <T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<Integer, T> nameToConstant = new HashMap<>();
    private final Map<T, Integer> constantToName = new HashMap<>();

    public EnumTypeAdapter(Class<T> classOfT) {
        for (T constant : classOfT.getEnumConstants()) {
            Integer name = constant.ordinal();
            nameToConstant.put(name, constant);
            constantToName.put(constant, name);
        }
    }
    @Override public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return nameToConstant.get(in.nextInt());
    }

    @Override public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : constantToName.get(value));
    }

    public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass(); // handle anonymous subclasses
            }
            return (TypeAdapter<T>) new EnumTypeAdapter(rawType);
        }
    };
}
