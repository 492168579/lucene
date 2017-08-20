import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by yezhaoyi on 2017/8/20.
 */
public class Test {

    public static  void CreateIndex() throws IOException {

        //索引存放的位置，设置在当前目录中
        Directory directory = FSDirectory.open(Paths.get("indexDir/"));


        //创建lucene的分词器，主要用于进行分词，比如识别你好，中国，甚至一些以前没有，但现在出先的词
        Analyzer analyzer = new StandardAnalyzer();
        //创建索引写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        //创建索引写入对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //创建Document对象，存储索引

        Document doc = new Document();
        /**
         * @param:name:字段名
         * @param:value:字段值
         * @param:store:是否存储
         */


        int iD = 6;
        Field id = new IntPoint("id",iD);
        Field storeField = new StoredField("id", iD);
        Field title = new StringField("title","ImportNew - 专注Java & Android 技术分享", Field.Store.YES);
        Field content = new TextField("content","ImportNew 是一个专注于 Java & Android 技术分享的博客," +
                "为Java 和 Android开发者提供有价值的内容。" +
                "包括:Android开发与快讯、Java Web开发和其他的Java技术相关的", Field.Store.YES);

        //将字段加入到doc中
        doc.add(id);
        doc.add(title);
        doc.add(content);
        doc.add(storeField);

        //将doc对象保存到索引库中
        indexWriter.addDocument(doc);

        //关闭流
        indexWriter.close();

    }
    public static  void SelectIndex() throws IOException{

        //索引存放的位置
        Directory directory = FSDirectory.open(Paths.get("indexDir/"));
        //创建索引的读取器
        IndexReader indexReader = DirectoryReader.open(directory);

        //创建一个索引的查找器，来检索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //这是一个条件查询的api，用于添加条件
        Term term = new Term("title","ImportNew - 专注Java & Android 技术分享");
        TermQuery termQuery = new TermQuery(term);

        //搜索先搜索索引库

        //返回符合条件的前100条记录
        TopDocs topDocs =  indexSearcher.search(termQuery, 100);

        //打印查询到的记录数
        System.out.println("总记录数:"+topDocs.totalHits);

        //得到得分文档数组
        ScoreDoc scoreDocs[] = topDocs.scoreDocs;

        //遍历数组，返回一个击中
        for(ScoreDoc scoreDoc : scoreDocs){

            int docID = scoreDoc.doc;

            //取得对应的文档对象
            Document document = indexSearcher.doc(docID);

            System.out.println(document.get("id"));
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
        }

    }
    public  static  void  main(String[] args){
        try {
            SelectIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
