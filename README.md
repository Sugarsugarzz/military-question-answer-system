### 1. 版本历史	

| 修订日期   | 版本号 | 修订人 | 备注                           |
| ---------- | ------ | ------ | ------------------------------ |
| 2020.08.07 | 1.0    | 唐     |                                |
| 2020.09.01 | 1.1    | 唐     | 补充别名处理方法说明及问句类型 |
| 2020.09.21 | 1.2    | 唐     | 补充期刊、报告的查询模式       |

### 2. 调用说明

- 调用前提

  需能够访问数据库服务器，并将data文件夹放在项目根目录，作为分词器词典。

  添加jar包依赖，在程序中调用方法即可。

#### 2.1 问答方法

- 调用类方法

  ```java
  String qa_main(String uid, String question, String q_time)
  ```

- 调用参数说明

  | 参数名   | 参数类型 | 说明                                              |
  | -------- | -------- | ------------------------------------------------- |
  | uid      | String   | 用户id                                            |
  | question | String   | 问句                                              |
  | q_time   | String   | 用户提问时间（推荐统一 yyyy-MM-dd HH:mm:ss 格式） |

- 调用示例

  ```java
  MultiMilitaryQA QA = new MultiMilitaryQA();
  String answer = QA.qa_main("用户id", "中国的战斗机有哪些？", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  ```

- 返回结果

  每次问答结果以 **JSON** 字符串返回。

#### 2.2 别名处理方法

##### 2.2.1. 自定义别名更新到对应的 sameas 别名表中

1. comcepts 别名

   data/dict_for_sameas/concepts_alias.txt 存放 concepts 的别名，可自行修改补充。

   调用该方法，即可将别名更新到 concept_sameas 表。

   ```java
   DbOperator.addConceptAliasToDB();
   ```

2. entity 别名抽取

   数据库表 entities 存放所有实体名。

   调用该方法，即可对所有实体名进行别名抽取，并更新到 entity_sameas 表。

   ```java
   DbOperator.addEntityAliasToDB();
   ```

##### 2.2.2 将 文件或数据库 中别名与原名的映射关系，更新到 match_dict 表

1. country、compare、most

   本地 data/dict_for_basic 目录下 country.txt、compare.txt、most.txt，可自行补充。

   调用该方法，即可将城市、比较词、最值词，以及别名，更新到 match_dict 表。

   ```java
   DbOperator.getCountryCompareMostToDB();
   ```

2. attribute、big_category、small_category

   数据库表 concepts 中 level 0，2，3 分别对应 attribute，big_category 和 small_category。

   数据库表 concept_sameas 存放所有 concept 的别名。

   调用此方法，即可将属性、一级分类和二级分类，以及别名，更新到 match_dict 表。

   ```java
   DbOperator.getConceptsAndSameasToDB();
   ```

3. entity

   数据库表 entities 存放所有实体。

   数据库表 entity_sameas 存放所有实体别名。

   调用此方法，即可将所有实体以及别名，更新到 match_dict 表。

   ```java
   DbOperator.getEntitiesAndSameasToDB();
   ```

##### 2.2.3 获取 match_dict 表到本地分词器词典

​	数据库表 match_dict 存放所有原名与别名的映射关系。

​	调用此方法，即可将所有别名从数据库获取，更新到本地分词器词典中。

```java
DbOperator.getDBToSegmentDict();
```

##### 2.2.4 使用场景

1. entities 表中的实体进行了更新补充

   首先抽取并更新实体的别名，再更新至 match_dict 表，最后将 match_dict 获取到本地分词器。

   ```java
   DbOperator.addEntityAliasToDB();
   DbOperator.getEntitiesAndSameasToDB();
   DbOperator.getDBToSegmentDict();
   ```

2. concepts 表的别名进行了自定义

   首先更新concept的别名，再更新至 match_dict 表，最后将 match_dict 获取到本地分词器。

   ```java
   DbOperator.addConceptAliasToDB();
   DbOperator.getConceptsAndSameasToDB();
   DbOperator.getDBToSegmentDict();
   ```

### 3. 支持问句类型

1. 一级类别名

   > 枪？
   >
   > 坦克？

2. 二级类别名

   > 战斗机有哪些？
   >
   > 自动步枪有哪些？
   >
   > 主战坦克有哪些？

3. 国家及二级类别名

   > 中国的战斗机有哪些？
   >
   > 苏联制造的自动步枪有哪些？

4. 单实体

   > 我想找神舟七号。

5. 多实体（对比）

   > 我想找神舟五号和神舟七号。
   >
   > 对比一下神舟五号和神舟七号。

6. 单实体单属性/多属性

   > 神舟七号的长度、发射地点、生产商、原产国、简介？

7. 多实体单属性/多属性

   > 神舟五号、神舟七号和歼20的长度、简介？

8. 全类别属性最值

   > 长度最长的是哪个？

9. 单类别属性最值

   > 战斗机里长度最短的是哪个？

10. 单类别单属性单区间

    > 长度大于25米的战斗机有哪些？
    >
    > 发射日期大于2011年的宇宙飞船？
    >
    > 歼击机里首飞时间在2011年之后的有哪些？

11. 单类别单属性多区间

    > 发射日期小于2010年且大于2005年的宇宙飞船？

12. 单类别多属性单区间

    > 长度大于25米且高度小于5米的战斗机有哪些？
    >
    > 首飞时间晚于2010年且高度大于4米的战斗机有哪些？

13. 热点查询模式（含关键词”热点“）

    > 上周有关歼20战斗机的热点
    >
    > 去年五月有关歼20战斗机的热点

14. 期刊查询模式（含关键词”期刊“）

    > 上周有关歼20战斗机的期刊
    >
    > 去年五月有关歼20战斗机的期刊

15. 报告查询模式（含关键词”报告“）

    > 上周有关歼20战斗机的报告
    >
    > 去年五月有关歼20战斗机的报告

16. 直达模式（支持跳转至 头条、百科、订阅、我的收藏、浏览历史）

    > 打开浏览历史
    >
    > 前往我的收藏

### 4. 问答方法返回字段说明

#### 4.1 百科模式（Q_type = 1）

​	从百科中返回答案涉及的实体或属性。

​	如果 A_type = 1，则重点展示搜索到的实体属性，可以利用实体id显示相应实体词条；如果 A_type = 2，则直接展示相应实体词条，对于答案结果数量过多的做一些筛除。

| 字段名 | 字段类型  | 说明                                                         | 是否一定存在 |
| ------ | --------- | ------------------------------------------------------------ | ------------ |
| Q_type | Integer   | 必为 1                                                       | 是           |
| A_type | Integer   | 1：属性  2：实体                                             | 是           |
| Answer | JSONArray | entity_id（Integer） 实体id<br />entity_name（String） 实体名<br />attr_name（String） 属性名（A_type=2时无此字段）<br />attr_value（String）属性值（A_type=2时无此字段） | 是           |

- 示例

  1. Question：**歼5和歼20的长度是多少？**

     ```json
     {
     	"Q_type": 1,
     	"A_type": 1,
     	"Answer": [{
     		"entity_name": "歼-5战斗机",
     		"attr_name": "长度",
     		"entity_id": 2560,
     		"attr_value": "11.68米"
     	}, {
     		"entity_name": "歼-20战斗机",
     		"attr_name": "长度",
     		"entity_id": 2847,
     		"attr_value": "20.3米"
     	}]
     }
     ```

  2. Question：**长度大于25米的战斗机有哪些？**

     ```json
     {
     	"Q_type": 1,
     	"A_type": 2,
     	"Answer": [{
     		"entity_name": "XF-108轻剑战斗机",
     		"entity_id": 2385
     	}, {
     		"entity_name": "YF-12战斗机",
     		"entity_id": 2036
     	}]
     }
     ```

#### 4.2 对比模式（Q_type = 2）

​	从百科中返回需要对比的实体。

​	根据实体id展示需要对比的实体，对相同的属性进行比对比较。

| 字段名 | 字段类型  | 说明                                                         | 是否一定存在 |
| ------ | --------- | ------------------------------------------------------------ | ------------ |
| Q_type | Integer   | 必为2                                                        | 是           |
| Answer | JSONArray | entity_id（Integer） 实体id<br />entity_name（String） 实体名 | 是           |

- 示例

  Question：**我想对比一下神舟五号和神舟六号**

  ```json
  {
  	"Q_type": 2,
  	"Answer": [{
  		"entity_name": "神舟五号",
  		"entity_id": 37
  	}, {
  		"entity_name": "神舟六号载人飞船",
  		"entity_id": 36
  	}]
  }
  ```

当对比项有**多个近似词条**时，也会同时返回。可以取第一个和最后一个进行前台属性比较，其余近似词条可以在其他对比板块，进行两两比较。

- 示例

  Question：**我想对比一下巴雷特和神舟七号**

  ```java
  {
  	"Q_type": 2,
  	"Answer": [{
  		"entity_name": "巴雷特XM109狙击步枪",
  		"entity_id": 95
  	}, {
  		"entity_name": "巴雷特M90狙击步枪",
  		"entity_id": 58
  	}, {
  		"entity_name": "巴雷特M98B狙击步枪",
  		"entity_id": 151
  	}, {
  		"entity_name": "巴雷特Light Fifty M82A1狙击步枪",
  		"entity_id": 132
  	}, {
  		"entity_name": "神舟七号",
  		"entity_id": 35
  	}]
  }
  ```

#### 4.3 热点查询模式（Q_type = 3）

​	通过语义解析提取问句中的时间及搜索关键词，从而在搜索库中查找相应热点资讯。

​	时间抽取为左闭右开方式。

| 字段名       | 字段类型           | 说明                                                         | 是否一定存在 |
| ------------ | ------------------ | ------------------------------------------------------------ | ------------ |
| Q_type       | Integer            | 必为3                                                        | 是           |
| Q_content    | List&lt;String&gt; | 搜索关键词（无法解析出关键词时，列表可能为空）               | 是           |
| Q_start_time | String             | 起始时间（问句中不涉及时间，则不存在该字段）                 | 否           |
| Q_end_time   | String             | 结束时间（无法从语义中解析结束时间，则不存在该字段，需要后台补充一个结束时间） | 否           |

- 示例

  Question：**去年五月至七月有关歼20战斗机的热点**

  ```json
  {
  	"Q_type": 3,
  	"Q_content": ["歼20", "战斗机"],
  	"Q_start_time": "2019-05-01 00:00:00",
    "Q_end_time": "2019-07-01 00:00:00"
  }
  ```

#### 4.4 期刊查询模式（Q_type = 4）

​	通过语义解析提取问句中的时间及搜索关键词，从而在搜索库中查找相应期刊。

​	时间抽取为左闭右开方式。

| 字段名       | 字段类型           | 说明                                                         | 是否一定存在 |
| ------------ | ------------------ | ------------------------------------------------------------ | ------------ |
| Q_type       | Integer            | 必为4                                                        | 是           |
| Q_content    | List&lt;String&gt; | 搜索关键词（无法解析出关键词时，列表可能为空）               | 是           |
| Q_start_time | String             | 起始时间（问句中不涉及时间，则不存在该字段）                 | 否           |
| Q_end_time   | String             | 结束时间（无法从语义中解析结束时间，则不存在该字段，需要后台补充一个结束时间） | 否           |

- 示例

  Question：**上周有关俄罗斯米格战斗机的期刊**

  ```json
  {
  	"Q_type": 4,
  	"Q_content": ["俄罗斯", "战斗机", "米格"],
  	"Q_start_time": "2020-09-14 00:00:00",
    "Q_end_time": "2020-09-21 00:00:00",
  }
  ```

#### 4.5 报告查询模式（Q_type = 5）

​	通过语义解析提取问句中的时间及搜索关键词，从而在搜索库中查找相应报告。

​	时间抽取为左闭右开方式。

| 字段名       | 字段类型           | 说明                                                         | 是否一定存在 |
| ------------ | ------------------ | ------------------------------------------------------------ | ------------ |
| Q_type       | Integer            | 必为5                                                        | 是           |
| Q_content    | List&lt;String&gt; | 搜索关键词（无法解析出关键词时，列表可能为空）               | 是           |
| Q_start_time | String             | 起始时间（问句中不涉及时间，则不存在该字段）                 | 否           |
| Q_end_time   | String             | 结束时间（无法从语义中解析结束时间，则不存在该字段，需要后台补充一个结束时间） | 否           |

- 示例

  Question：**前年到今天有关美国M1坦克的报告**

  ```json
  {
  	"Q_type": 5,
  	"Q_content": ["M1", "坦克", "美国"],
  	"Q_start_time": "2018-01-01 00:00:00",
  	"Q_end_time": "2020-09-21 00:00:00"
  }
  ```

#### 4.6 直达模式（Q_type = 6）

​	通过语义解析用户的跳转意图。

| 字段名 | 字段类型 | 说明                                                | 是否一定存在 |
| ------ | -------- | --------------------------------------------------- | ------------ |
| Q_type | Integer  | 必为4                                               | 是           |
| Answer | Integer  | 1：头条  2：百科  3：订阅  4：我的收藏  5：浏览历史 | 是           |

- 示例

  Question：**打开我的收藏**

  ```json
  {
  	"Q_type": 6,
  	"Answer": 4
  }
  ```

#### 4.7 辅助模式（Q_type = 7）

​	用户输入一个较大概念的类别名词（一级类别），返回用户一些较小概念的类别名（二级类别），前台可以提示用户使用这些二级类别名来搜索，从而达到辅助用户使用问答系统的目的。

| 字段名 | 字段类型           | 说明           | 是否一定存在 |
| ------ | ------------------ | -------------- | ------------ |
| Q_type | Integer            | 必为7          | 是           |
| Answer | List&lt;String&gt; | 二级类别名列表 | 是           |

- 示例

  Question：**枪**

  ```json
  {
  	"Q_type": 7,
  	"Answer": ["非自动步枪", "自动步枪", "冲锋枪", "狙击枪", "手枪", "机枪", "霰弹枪", "火箭筒", "榴弹发射器"]
  }
  ```

#### 4.8 未检索到答案的情况

​	无论 **Q_type** 为多少，**Answer字段为空** 即为未找到答案。

