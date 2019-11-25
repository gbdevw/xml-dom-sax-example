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
     * Retiens l'element examine.
     */
    private String currElementName;

    /**
     * Logger pour la classe
     */
    private final Logger Log = LoggerFactory.getLogger(PrintXmlDocSaxHandler.class);

    /**
     * Appele a chaque fois que le parser SAX lit la balise de debut d'un element.
     * 
     * @param uri Namespace de l'element - null si aucun namespace est utilise (c'est le cas ici)
     * @param localName Nom de l'element - null si aucun namespace est utilise (c'est le cas ici)
     * @param qName Nom de l'element avec prefixe s'il y en a un
     * @param attributes Liste des attributs de l'element
     * 
     * @throws SAXException Exception pouvant etre lancee par la methode en cas d'erreur
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        // Retenir quel element est examine
        this.currElementName = qName;

        // Afficher evenement : Debut element
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
     * Appele a chaque fois que le parser SAX lit la balise de fin d'un element.
     * 
     * @param uri Namespace de l'element - null si aucun namespace est utilise (c'est le cas ici)
     * @param localName Nom de l'element - null si aucun namespace est utilise (c'est le cas ici)
     * @param qName Nom de l'element avec prefixe s'il y en a un
     * 
     * @throws SAXException Exception pouvant etre lancee par la methode en cas d'erreur
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // Afficher evenement : Fin element
        this.Log.info("ELEMENT (END) : " + qName);
    }

    /**
     * Appele a chaque fois que le parser SAX lit le contenu textuel d'un element
     * 
     * @param ch Contenu du fichier lu sous forme de texte
     * @param start Indice pour demarrer la lecture du contenu texte de l'element examine
     * @param length Longueur du contenu texte de l'element examine
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        // Toujours recuperer le contenu texte de l'element de cette maniere.
        String textContent = new String(ch, start, length);

        // Afficher le contenu texte de l'element
        this.Log.info("ELEMENT : " + currElementName + " - TEXT : " + textContent);
    }

    /**
     * Appele lorsque le parser SAX demarre le parsing d'un document
     */
    @Override
    public void startDocument() throws SAXException {

        // Afficher evenement : Debut document
        this.Log.info("DOCUMENT : BEGIN");
    }

    /**
     * Appele lorsque le parser SAX finit le parsing d'un document
     */
    @Override
    public void endDocument() throws SAXException {

        // Afficher evenement : Fin document
        this.Log.info("DOCUMENT : END");
    }
}