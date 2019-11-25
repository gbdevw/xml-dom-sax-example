package masters.xmllab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler pour parser SAX qui affiche le contenu et la structure du fichier XML
 * parcouru.
 */
public class PrintXmlDocSaxHandler extends DefaultHandler {

    /**
     * Retiens l'élément examiné.
     */
    private String currElementName;

    /**
     * Logger pour la classe
     */
    private final Logger Log = LoggerFactory.getLogger(PrintXmlDocSaxHandler.class);

    /**
     * Appelé à chaque fois que le parser SAX lit la balise de début d'un élément.
     * 
     * @param uri Namespace de l'élément - null si aucun namespace est utilisé (c'est le cas ici)
     * @param localName Nom de l'élément - null si aucun namespace est utilisé (c'est le cas ici)
     * @param qName Nom de l'élément avec préfixe s'il y en a un
     * @param attributes Liste des attributs de l'élément
     * 
     * @throws SAXException Exception pouvant être lancée par la méthode en cas d'erreur
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        // Retenir quel élément est examiné
        this.currElementName = qName;

        // Afficher événement : Début élément
        this.Log.info("ELEMENT (BEGIN) : " + qName);

        // Afficher les attributs s'il y en a
        boolean hasAttributes = attributes.getLength() > 0 ? true : false;
        this.Log.info("Element has attributes : " + hasAttributes);
        for(int c0 = 0; c0 < attributes.getLength(); c0++) {
            String attrName = attributes.getLocalName(c0);
            String attrValue = attributes.getValue(c0);
            this.Log.info("ATTRIBUTE : " + attrName + " - VALUE : " + attrValue);
        }
    }

    /**
     * Appelé à chaque fois que le parser SAX lit la balise de fin d'un élément.
     * 
     * @param uri Namespace de l'élément - null si aucun namespace est utilisé (c'est le cas ici)
     * @param localName Nom de l'élément - null si aucun namespace est utilisé (c'est le cas ici)
     * @param qName Nom de l'élément avec préfixe s'il y en a un
     * 
     * @throws SAXException Exception pouvant être lancée par la méthode en cas d'erreur
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // Afficher événement : Fin élément
        this.Log.info("ELEMENT (END) : " + qName);
    }

    /**
     * Appelé à chaque fois que le parser SAX lit le contenu textuel d'un élément
     * 
     * @param ch Contenu du fichier lu sous forme de texte
     * @param start Indice pour démarrer la lecture du contenu texte de l'élément examiné
     * @param length Longueur du contenu texte de l'élément examiné
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        // Toujours récupérer le contenu texte de l'élément de cette manière.
        String textContent = new String(ch, start, length);

        // Afficher le contenu texte de l'élément
        this.Log.info("ELEMENT : " + currElementName + " - TEXT : " + textContent);
    }

    /**
     * Appelé lorsque le parser SAX démarre le parsing d'un document
     */
    @Override
    public void startDocument() throws SAXException {

        // Afficher événement : Début document
        this.Log.info("DOCUMENT : BEGIN");
    }

    /**
     * Appelé lorsque le parser SAX finit le parsing d'un document
     */
    @Override
    public void endDocument() throws SAXException {

        // Afficher événement : Fin document
        this.Log.info("DOCUMENT : END");
    }
}