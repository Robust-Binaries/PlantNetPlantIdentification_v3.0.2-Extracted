package com.facebook.react.bridge;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;

public class JsonWriter implements Closeable {
    private final Deque<Scope> mScopes = new ArrayDeque();
    private final Writer mWriter;

    private enum Scope {
        EMPTY_OBJECT,
        OBJECT,
        EMPTY_ARRAY,
        ARRAY
    }

    public JsonWriter(Writer writer) {
        this.mWriter = writer;
    }

    public JsonWriter beginArray() throws IOException {
        open(Scope.EMPTY_ARRAY, '[');
        return this;
    }

    public JsonWriter endArray() throws IOException {
        close(']');
        return this;
    }

    public JsonWriter beginObject() throws IOException {
        open(Scope.EMPTY_OBJECT, '{');
        return this;
    }

    public JsonWriter endObject() throws IOException {
        close('}');
        return this;
    }

    public JsonWriter name(String str) throws IOException {
        if (str != null) {
            beforeName();
            string(str);
            this.mWriter.write(58);
            return this;
        }
        throw new NullPointerException("name can not be null");
    }

    public JsonWriter value(String str) throws IOException {
        if (str == null) {
            return nullValue();
        }
        beforeValue();
        string(str);
        return this;
    }

    public JsonWriter nullValue() throws IOException {
        beforeValue();
        this.mWriter.write("null");
        return this;
    }

    public JsonWriter rawValue(String str) throws IOException {
        beforeValue();
        this.mWriter.write(str);
        return this;
    }

    public JsonWriter value(boolean z) throws IOException {
        beforeValue();
        this.mWriter.write(z ? "true" : "false");
        return this;
    }

    public JsonWriter value(double d) throws IOException {
        beforeValue();
        this.mWriter.append(Double.toString(d));
        return this;
    }

    public JsonWriter value(long j) throws IOException {
        beforeValue();
        this.mWriter.write(Long.toString(j));
        return this;
    }

    public JsonWriter value(Number number) throws IOException {
        if (number == null) {
            return nullValue();
        }
        beforeValue();
        this.mWriter.append(number.toString());
        return this;
    }

    public void close() throws IOException {
        this.mWriter.close();
    }

    private void beforeValue() throws IOException {
        Scope scope = (Scope) this.mScopes.peek();
        switch (scope) {
            case EMPTY_ARRAY:
                replace(Scope.ARRAY);
                return;
            case EMPTY_OBJECT:
                throw new IllegalArgumentException(Scope.EMPTY_OBJECT.name());
            case ARRAY:
                this.mWriter.write(44);
                return;
            case OBJECT:
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown scope: ");
                sb.append(scope);
                throw new IllegalStateException(sb.toString());
        }
    }

    private void beforeName() throws IOException {
        Scope scope = (Scope) this.mScopes.peek();
        switch (scope) {
            case EMPTY_ARRAY:
            case ARRAY:
                throw new IllegalStateException("name not allowed in array");
            case EMPTY_OBJECT:
                replace(Scope.OBJECT);
                return;
            case OBJECT:
                this.mWriter.write(44);
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown scope: ");
                sb.append(scope);
                throw new IllegalStateException(sb.toString());
        }
    }

    private void open(Scope scope, char c) throws IOException {
        this.mScopes.push(scope);
        this.mWriter.write(c);
    }

    private void close(char c) throws IOException {
        this.mScopes.pop();
        this.mWriter.write(c);
    }

    private void string(String str) throws IOException {
        this.mWriter.write(34);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case 8:
                    this.mWriter.write("\\b");
                    break;
                case 9:
                    this.mWriter.write("\\t");
                    break;
                case 10:
                    this.mWriter.write("\\n");
                    break;
                case 12:
                    this.mWriter.write("\\f");
                    break;
                case 13:
                    this.mWriter.write("\\r");
                    break;
                case '\"':
                case '\\':
                    this.mWriter.write(92);
                    this.mWriter.write(charAt);
                    break;
                case 8232:
                case 8233:
                    this.mWriter.write(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                    break;
                default:
                    if (charAt > 31) {
                        this.mWriter.write(charAt);
                        break;
                    } else {
                        this.mWriter.write(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                        break;
                    }
            }
        }
        this.mWriter.write(34);
    }

    private void replace(Scope scope) {
        this.mScopes.pop();
        this.mScopes.push(scope);
    }
}
