public static void main(String[] args) {
    Language language = // Target language produced by the Language Workbench
    String sourceCode = // Source code written by the user
    try {
        language.parse(sourceCode);
    } catch (ParseException e) {
        // Send diagnostic information to the language client
    }
}
