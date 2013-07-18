package es.us.isa.cristal.owl;

public class OntologyNamespaces {
    private PrefixNamespacePair person;
    private PrefixNamespacePair group;
    private PrefixNamespacePair activity;

    public PrefixNamespacePair getPerson() {
        return person;
    }

    public PrefixNamespacePair getGroup() {
        return group;
    }

    public PrefixNamespacePair getActivity() {
        return activity;
    }

    public void setPerson(String prefix, String namespace) {
        this.person = new PrefixNamespacePair(prefix, namespace);
    }

    public void setGroup(String prefix, String namespace) {
        this.group = new PrefixNamespacePair(prefix, namespace);
    }

    public void setActivity(String prefix, String namespace) {
        this.activity = new PrefixNamespacePair(prefix, namespace);
    }

    public class PrefixNamespacePair {
        private String prefix;
        private String namespace;

        private PrefixNamespacePair(String prefix, String namespace) {
            this.prefix = prefix;
            this.namespace = namespace;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getNamespace() {
            return namespace;
        }
    }
}
