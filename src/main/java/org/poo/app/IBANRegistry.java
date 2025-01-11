package org.poo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBANRegistry {
    private Map<String, List<String>> ibanAliases;

    /**
     * Initializes an empty registry for IBAN aliases.
     */
    public IBANRegistry() {
        ibanAliases = new HashMap<String, List<String>>();
    }

    /**
     * Registers an IBAN with a specified alias. If the alias already exists, the IBAN is added
     * to the list of IBANs associated with that alias.
     *
     * @param iban  the IBAN to register, must not be null or empty.
     * @param alias the alias to associate with the IBAN, must not be null or empty.
     */
    public void registerIBAN(final String iban, final String alias) {
        if (iban == null || iban.trim().isEmpty() || alias == null || alias.trim().isEmpty()) {
            System.out.println("IBAN cannot be null or empty");
            return;
        }

        ibanAliases.computeIfAbsent(alias, k -> new ArrayList<String>()).add(iban);
    }

    /**
     * Updates the alias associated with an IBAN. If the current alias is valid and the IBAN exists,
     * the IBAN is removed from the current alias and associated with the new alias.
     *
     * @param currentIdentifier the current alias or IBAN identifier to update.
     * @param newAlias          the new alias to associate with the IBAN.
     * @return {@code true} if the alias was updated successfully; {@code false} otherwise.
     */
    public boolean updateAlias(final String currentIdentifier, final String newAlias) {
        if (currentIdentifier == null || newAlias == null) {
            return false;
        }
        String existingIban = getIBAN(currentIdentifier);

        if (existingIban == null) {
            return false;
        }

        if (!removeIBAN(currentIdentifier)) {
            return false;
        }
        registerIBAN(existingIban, newAlias);

        return true;
    }

    /**
     * Retrieves the IBAN associated with a given alias or IBAN identifier.
     *
     * @param identifier the alias or IBAN to search for.
     * @return the IBAN if found; {@code null} otherwise.
     */
    public String getIBAN(final String identifier) {
        if (ibanAliases.containsKey(identifier)) {
            return ibanAliases.get(identifier).getLast();
        }

        for (Map.Entry<String, List<String>> entry : ibanAliases.entrySet()) {
            if (entry.getValue().contains(identifier)) {
                return identifier;
            }
        }

        return null;
    }

    /**
     * Removes an IBAN from the registry. If the IBAN is the last entry under a specific alias,
     * the alias is also removed.
     *
     * @param identifier the alias or IBAN to remove.
     * @return {@code true} if the IBAN was removed successfully; {@code false} otherwise.
     */
    public boolean removeIBAN(final String identifier) {
        if (identifier == null) {
            return false;
        }

        for (Map.Entry<String, List<String>> entry : ibanAliases.entrySet()) {
            if (entry.getValue().remove(identifier)) {
                if (entry.getValue().isEmpty()) {
                    ibanAliases.remove(entry.getKey());
                }
                return true;
            }
        }

        return false;
    }
}
