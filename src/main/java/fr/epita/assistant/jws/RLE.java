package fr.epita.assistant.jws;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RLE {

    public static List<String> wrappEncode(final String str) {
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {

            if (builder.length() == 17) {
                list.add(new String(builder));
                builder.setLength(0); // reset the builder
            }
            builder.append(str.charAt(i));
        }
        list.add(new String(builder));
        return list.stream().map(RLE::encode).collect(Collectors.toList());
    }

    private static String encode(final String str) {
        int j = 0;
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            for (; j < str.length() && str.charAt(i) == str.charAt(j); ++j) {
                ++count;
                if (count >= 9) {
                    builder.append(count).append(str.charAt(i));
                    count = 0;
                }
            }
            builder.append(count).append(str.charAt(i));
            count = 0;
            i = j - 1;
        }
        return new String(builder);
    }

    public static List<String> wrappDecode(final String path) {
        List<String> map = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                map.add(RLE.decode(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String decode(final String str) {
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            builder.append(String.valueOf(str.charAt(i + 1)).repeat(str.charAt(i) % '0'));
        }
        return new String(builder);
    }
}
