package main.java.data;

import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {
    public static final String path = "./data/";

    public String name;
    public Set<String> categories;
    public Set<String> ages;
    public Set<String> genders;
    public Set<String> experiencedCountries;
    public Set<String> preferences;
    public String country;
    public String link;

    public static List<Program> allPrograms;

    public static void readProgramsFromFiles() {
        allPrograms = new ArrayList<>();

        final File folder = new File(path);
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".txt")) {
                System.out.println(fileEntry.getName());
                Program program = new Program();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

                    program.name = bufferedReader.readLine();
                    program.categories = Sets.newHashSet(bufferedReader.readLine().split(" "));
                    program.ages = Sets.newHashSet(bufferedReader.readLine().split(" "));
                    program.genders = Sets.newHashSet(bufferedReader.readLine().split(" "));
                    program.experiencedCountries = Sets.newHashSet(bufferedReader.readLine().split(" "));
                    program.preferences = Sets.newHashSet(bufferedReader.readLine().split(" "));
                    program.country = bufferedReader.readLine();
                    program.link = bufferedReader.readLine();
                    allPrograms.add(program);

                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Program> getSelectedProgram(String gender, String age,
                                                   Set<String> countryPreferredSet,
                                                   Set<String> countryExperiencedSet,
                                                   Set<String> preferenceSet) {
        Stream<Program> stream = allPrograms.stream();
        if (gender != null) {
            stream = stream.filter(program -> program.genders.contains(gender));
        }
        if (age != null) {
            stream = stream.filter(program -> program.ages.contains(age));
        }
        if (!countryPreferredSet.isEmpty()) {
            stream = stream.filter(program -> countryPreferredSet.contains(program.country));
        }
        if (!countryExperiencedSet.isEmpty()) {
            stream = stream.filter(program -> {
                Set<String> set = Sets.newHashSet(countryExperiencedSet);
                set.retainAll(program.experiencedCountries);
                return !set.isEmpty();
            });
        }
        if (!preferenceSet.isEmpty()) {
            stream = stream.filter(program -> {
                Set<String> set = Sets.newHashSet(preferenceSet);
                set.retainAll(program.preferences);
                return !set.isEmpty();
            });
        }
        return stream.collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.categories + "   " + this.name + "   " + this.country + "   " + this.link;
    }
}
