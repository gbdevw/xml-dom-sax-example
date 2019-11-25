package masters.xmllab;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Application de demonstration de DOM et SAX
 */
public class App {

    // Instancier un logger
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    /**
     * L'application charge un fichier XML depuis les ressources embrquees
     * dans le JAR et le lit au moyen d'un parser DOM et d'un parser SAX. A
     * chaque fois, l'application parcourt et affiche la structure et le 
     * contenu du fichier.
     * 
     * @param args Pas utilise
     */
    public static void main(String[] args) {

        // Variable qui contient le flux vers le fichier XML
        InputStream file;

        /* Debut de l'application */
        LOG.info("Starting application ...");
        LOG.trace("Opening file resources/example.xml");

        try {
            
            /*********************************************************************/
            /*                                  PARTIE DOM                       */
            /*********************************************************************/

            // Charger le fichier example.xml depuis les ressources embarquees dans
            // le JAR (repertoire src/main/java/resources).
            file = App.class.getResourceAsStream("/example-minified.xml");
            
            // Logger debut de l'analyse du fichier avec DOM
            LOG.info("Starting DOM Demo ...");

            // Instancier la factory qui permet de creer un parser DOM
            LOG.trace("Create the factory that creates DOM parsers");
            DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();

            // Instancier le parser qui permet d'obtenir un document DOM à partir
            // du fichier XML.
            LOG.trace("Create a DOM parser");
            DocumentBuilder domParser = domParserFactory.newDocumentBuilder();

            // Utiliser le parser pour creer un nouveau document DOM
            LOG.trace("Parse the file with the DOM parser");
            Document doc = domParser.parse(file);

            // Analyser la declaration XML
            LOG.info("XML Version: " + doc.getXmlVersion());
            LOG.info("Encoding : " + doc.getXmlEncoding());

            // Parcourir et afficher l'arbre (depth-first)
            LOG.info("Processing the root node");
            Element root = doc.getDocumentElement();
            printDomTree(root);

            // Récupérer tous les noeuds correspondant a des element "data"
            LOG.info("Fetching \"data\" nodes");
            NodeList dataNodes = doc.getElementsByTagName("data");
            LOG.info("Found " + dataNodes.getLength() + " \"data\" nodes");

            // Analyser les noeuds obtenus
            for (int c0 = 0; c0 < dataNodes.getLength(); c0++) {
                LOG.info("Processing found data nodes [" + (c0+1) + "/" + dataNodes.getLength() + "]");

                // Analyser le parent du noeud s'il y en a
                boolean hasParent = dataNodes.item(c0).getParentNode() != null ? true : false;
                LOG.info("Data node has parent : " + hasParent);
                if(hasParent) { LOG.info("Parent name : " + dataNodes.item(c0).getParentNode().getNodeName()); }

                // Analyser le noeud frère précédent s'il y en a
                boolean hasPreviousSibling = dataNodes.item(c0).getPreviousSibling() != null ? true : false;
                LOG.info("Node has previous sibling : " + hasPreviousSibling);
                if(hasPreviousSibling) { 
                    LOG.info("Previous sibling name : " + dataNodes.item(c0).getPreviousSibling().getNodeName());
                    boolean previousSiblingHasTextContent = dataNodes.item(c0).getPreviousSibling().getTextContent() != null ? true : false;
                    LOG.info("Previous sibling has text content : " + previousSiblingHasTextContent);
                    LOG.info("Previous sibling text content : " + dataNodes.item(c0).getPreviousSibling().getTextContent());
                }

                // Analyser le noeud frère suivant s'il y en a
                boolean hasNextSibling = dataNodes.item(c0).getNextSibling() != null ? true : false;
                LOG.info("Node has next sibling : " + hasNextSibling);
                if(hasNextSibling) { 
                    LOG.info("Next sibling name : " + dataNodes.item(c0).getNextSibling().getNodeName());
                    boolean nextSiblingHasTextContent = dataNodes.item(c0).getNextSibling().getTextContent() != null ? true : false;
                    LOG.info("Next sibling has text content : " + nextSiblingHasTextContent);
                    LOG.info("Next sibling text content : " + dataNodes.item(c0).getNextSibling().getTextContent());
                }
            }

            // Fermer le fichier
            file.close();

            // Logger fin de la partie DOM
            LOG.info("Ending DOM Demo ...");

            /*********************************************************************/
            /*                                  PARTIE SAX                       */
            /*********************************************************************/

            LOG.info("Starting SAX Demo ...");

            // Charger le fichier example.xml depuis les ressources embarquees dans
            // le JAR (repertoire src/main/java/resources).
            file = App.class.getResourceAsStream("/example-minified.xml");

            // Instancier la factory qui crée les parsers SAX
            LOG.trace("Creating SAX parser factory");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

            // Créer un parser SAX
            LOG.trace("Creating SAX parser");
            SAXParser saxParser = saxParserFactory.newSAXParser();

            // Créer le handler qui affiche le contenu et la structure du document
            // XML pendant sa lecture par le parser SAX.
            LOG.trace("Creating the handler to use to print XML file");
            PrintXmlDocSaxHandler printXmlDocSacHandler = new PrintXmlDocSaxHandler();

            // Parcourir le fichier et effectuer le traitement
            LOG.info("Parsing the XML file ...");
            saxParser.parse(file, printXmlDocSacHandler);

            // Fermer le flux vers le fichier
            file.close();
            LOG.info("Parsing of the XML file - Done");

            // Charger le fichier example.xml depuis les ressources embarquees dans
            // le JAR (repertoire src/main/java/resources).
            file = App.class.getResourceAsStream("/example-minified.xml");

            // Créer le handler qui affiche l'identifiant des noeuds qui ne contiennent
            // pas d'élément data.
            LOG.trace("Creating the handler to use to query the XML file");
            QueryEmptyDataNodesSaxHandler qednSaxHandler = new QueryEmptyDataNodesSaxHandler();

            // Parcourir le fichier et effectuer le traitement
            LOG.info("Parsing the XML file ...");
            saxParser.parse(file, qednSaxHandler);

            // Fermer le flux vers le fichier
            file.close();
            LOG.info("Parsing of the XML file - Done");

            LOG.info("Ending SAX Demo ...");
            LOG.info("Ending application ...");
        }
        catch(Exception ex)
        {
            // Logger l'erreur
            LOG.error("An error occured", ex);
        }
    }

    /**
     * Affiche le contenu de l'arbre DOM. La fonction procede comme suit :
     * 
     * 1. Declarer debut du traitement, afficher nom du l'element traite et afficher les attributs.
     * 2. Si l'element a des fils, afficher le contenu de chaque fils
     * 3. Si l'element est un noeud texte, afficher la valeur
     * 4. Declarer fin du traitement du noeud
     * 
     * @param node Noeud racine de l'arbre DOM
     */
    private static void printDomTree (Node node) {

        // 1. Declarer debut du traitement
        LOG.info("ELEMENT (BEGIN) : " + node.getNodeName());
        LOG.info("ELEMENT has attributes : " + node.hasAttributes());

        // Afficher les attributs s'il y en a
        if(node.hasAttributes()) {

            for (int c1 = 0; c1 < node.getAttributes().getLength(); c1++) {
                Node currAttrNode = node.getAttributes().item(c1);
                String attrName = currAttrNode.getNodeName();
                String attrValue = currAttrNode.getNodeValue();
                LOG.info("ATTRIBUTE : " + attrName + " - VALUE : " + attrValue);
            }
        }

        // 2. Traiter les fils de l'element
        for(int c2 = 0; c2 < node.getChildNodes().getLength(); c2++)
        {
            printDomTree(node.getChildNodes().item(c2));
        }

        // 3. Si l'element est un noeud texte, afficher la valeur
        if(node.getNodeType() == Node.TEXT_NODE) {
            LOG.info("TEXT : " + node.getNodeValue());
        }

        // 4. Declarer fin du traitement
        LOG.info("ELEMENT (END) : " + node.getNodeName());
    }
}
