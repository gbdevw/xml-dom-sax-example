package masters.xmllab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Cette classe est un handler SAX qui detecte les noeud sans element data et affiche leur id.
 */
public class QueryEmptyDataNodesSaxHandler extends DefaultHandler {

    /**
     * Indique si l'element data est manquant du noeud examine
     */
    private boolean dataIsMissing;

    /**
     * Indique si le contenu de l'element examine appartient a un element id
     */
    private boolean idIsBeingProcessed;

    /**
     * Retiens la valeur de l'element id du noeud examine
     */
    private String nodeId;

    /**
     * Logger pour la classe
     */
    private final Logger Log = LoggerFactory.getLogger(QueryEmptyDataNodesSaxHandler.class);

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
      
        // Si l'element que l'on commence a examiner est un element noeud
        if(qName.equals("noeud")) {

            // Jusqu'a preuve du contraire, on suppose que l'element data manque
            this.dataIsMissing = true;
        }

        // Si l'element que l'on commence a examiner est un element id
        if(qName.equals("id")) {
            
            // Lever le flag qui indique que l'on examine un element id
            this.idIsBeingProcessed = true;
        }

        // Si l'element que l'on commence a examiner est un element data
        if(qName.equals("data")) {

            // Un element data a ete trouve
            this.dataIsMissing = false;
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

        // Si l'element que l'on a examine est un element noeud
        if(qName.equals("noeud")) {

            // Si aucun element data n'a ete trouve
            if(this.dataIsMissing) {
                // Afficher l'ID du noeud qui n'a pas d'element data
                Log.info("No \"data\" element for the node identified by : " + nodeId);
            }
        }

        // Si l'element que l'on commence a examiner est un element id
        if(qName.equals("id")) {
            
            // Baisser le flag qui indique que l'on examine un element id
            this.idIsBeingProcessed = false;
        }
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

        // Si l'element examine est un element id, retenir l'ID
        if(this.idIsBeingProcessed) {
            this.nodeId = textContent;
        }
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