package masters.xmllab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Cette classe est un handler SAX qui détecte les noeud sans élément data et affiche leur id.
 */
public class QueryEmptyDataNodesSaxHandler extends DefaultHandler {

    /**
     * Indique si l'élément data est manquant du noeud examiné
     */
    private boolean dataIsMissing;

    /**
     * Indique si le contenu de l'élément examiné appartient à un élément id
     */
    private boolean idIsBeingProcessed;

    /**
     * Retiens la valeur de l'élément id du noeud examiné
     */
    private String nodeId;

    /**
     * Logger pour la classe
     */
    private final Logger Log = LoggerFactory.getLogger(QueryEmptyDataNodesSaxHandler.class);

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
      
        // Si l'élément que l'on commence à examiner est un élément noeud
        if(qName.equals("noeud")) {

            // Jusqu'à preuve du contraire, on suppose que l'élément data manque
            this.dataIsMissing = true;
        }

        // Si l'élément que l'on commence à examiner est un élément id
        if(qName.equals("id")) {
            
            // Lever le flag qui indique que l'on examine un élément id
            this.idIsBeingProcessed = true;
        }

        // Si l'élément que l'on commence à examiner est un élément data
        if(qName.equals("data")) {

            // Un élément data a été trouvé
            this.dataIsMissing = false;
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

        // Si l'élément que l'on a examiné est un élément noeud
        if(qName.equals("noeud")) {

            // Si aucun élément data n'a été trouvé
            if(this.dataIsMissing) {
                // Afficher l'ID du noeud qui n'a pas d'élément data
                Log.info("No \"data\" element for the node identified by : " + nodeId);
            }
        }

        // Si l'élément que l'on commence à examiner est un élément id
        if(qName.equals("id")) {
            
            // Baisser le flag qui indique que l'on examine un élément id
            this.idIsBeingProcessed = false;
        }
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

        // Si l'élément examiné est un élément id, retenir l'ID
        if(this.idIsBeingProcessed) {
            this.nodeId = textContent;
        }
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