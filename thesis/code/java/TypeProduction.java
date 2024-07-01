@TypeAnnotation(TypeEnum.PRODUCTION)
public class TypeProduction extends DefaultTypeScope implements ExternalVisible<String> {

  String name = "";
  AtomicInteger nonTerminalCounter;
  AtomicInteger terminalCounter = new AtomicInteger(0);

  List<SymbolTableEntry> symbolsList = new ArrayList<>();

  @Override
  public String id() {
    return "production";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof ProductionSignature;
  }

  @Override
  public void bindTypeToIdentifier(String variable, SymbolTableEntry entry)
      throws UnbindableEntryException {
    Integer counter = null;

    if (entry.type() instanceof TypeTerminal || entry.type() instanceof TypeRegex) {
      counter = terminalCounter.getAndIncrement();
    } else if (entry.type() instanceof TypeNonTerminal) {
      counter = nonTerminalCounter.getAndIncrement();
    }

    if (counter != null && entry.type() instanceof TypeSymbol typeSymbol) {
      var identifier = typeSymbol.setup(counter, this);
      symbolsList.add(entry);
      super.bindTypeToIdentifier(identifier, entry);
    }

    if (entry.type() instanceof TypeLabel typeLabel) {
      var identifier =
          typeLabel.setup(variable, this);
      symbolsList.add(entry);
      super.bindTypeToIdentifier(identifier, entry);
    }

    if (entry.type() instanceof TypeRegex typeRegex) {
      var matcher = TypeRegex.REGEX_PATTERN.matcher(variable);
      if (matcher.find()) {
        var regexMatcher = TypeRegex.EXTRACT_REGEX.matcher(variable);
        if (regexMatcher.find()) {
          typeRegex.setRegex(regexMatcher.group(1));
        }
        if (matcher.groupCount() > 1) {
          typeRegex.setDescription(matcher.group(2));
        }
        if (matcher.groupCount() == 3) {
          var label = matcher.group(3);
          typeRegex.setLabel(label);
          super.bindTypeToIdentifier(label, entry);
        }
      }
    }
  }

}

