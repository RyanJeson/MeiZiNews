#AhSatyr

 - [���ܼ�����BlockCanary](https://github.com/moduth/blockcanary)
 - [MVP�����ܣ�MVPro](https://github.com/qibin0506/MVPro)
 - [��־��logger](https://github.com/orhanobut/logger)
 - [ RxJava ʹ�ã��� Android �����ߵ� RxJava ���](http://gank.io/post/560e15be2dca930e00da1083#toc_26)
 - [��������retrofit](https://github.com/square/retrofit)

---

#Retrofit ʹ��

##ת����

[retrofit](http://square.github.io/retrofit/#restadapter-configuration)
[RxAndroid+Retrofit�����](http://www.07net01.com/program/2016/02/1280821.html)

ת������??�Ա���ӵ�֧���������͡����ֵ�ģ����Ӧ���е����л���Ϊ���ṩ���㡣
Converters can be added to support other types. Six sibling modules adapt popular serialization libraries for your convenience.

	Gson: com.squareup.retrofit2:converter-gson
	Jackson: com.squareup.retrofit2:converter-jackson
	Moshi: com.squareup.retrofit2:converter-moshi
	Protobuf: com.squareup.retrofit2:converter-protobuf
	Wire: com.squareup.retrofit2:converter-wire
	Simple XML: com.squareup.retrofit2:converter-simplexml
	Scalars (primitives, boxed, and String): com.squareup.retrofit2:converter-scalars

ֱ�ӷ���String���������룺ScalarsConverterFactory.create()

	retrofit = new Retrofit.Builder()
			.client(MyOkHttpClient.getMyOkHttpClient().getOkHttpClient())//���ò�ͬ�ĵײ������
			.baseUrl(strBaseUrl)
			.addConverterFactory(ScalarsConverterFactory.create())//��� String����[ Scalars (primitives, boxed, and String)] ת����
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//��� RxJava ������
			.build();
			
����Gson���������룺GsonConverterFactory.create()

	retrofit = new Retrofit.Builder()
			.client(MyOkHttpClient.getMyOkHttpClient().getOkHttpClient())//���ò�ͬ�ĵײ������
			.baseUrl(strBaseUrl)
			.addConverterFactory(GsonConverterFactory.create())//��� json ת����
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//��� RxJava ������
			.build();

##Retrofit ��� RxJavaʹ��

[Retrofit ��� RxJavaʹ��](http://blog.csdn.net/ttccaaa/article/details/50659234)

 1. �������
		
		compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'
		compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
		
		compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
		
		compile 'io.reactivex:rxandroid:1.1.0'
		compile 'io.reactivex:rxjava:1.1.0'
		
	ǰ������ Retrofit �� Gson ���������������� Retrofit �� RxJava ת����������������������� RxJava �� Rx Android ������

 2. ��д API

	ʹ�� RxJava ������£��ӿ��ļ������޸ģ��ӿ��еķ����ķ������Ͳ����� Call ���� Observable ���ͣ�
		
		public interface GitHub {
		
		    @GET("/repos/{owner}/{repo}/contributors")
		    Call<List<Contributor>> contributors(@Path("owner") String owner,@Path("repo") String repo);
		
		    //ʹ�� RxJava �ķ���,����һ�� Observable
		    @GET("/repos/{owner}/{repo}/contributors")
		    Observable<List<Contributor>> RxContributors(@Path("owner") String owner,@Path("repo") String repo);
		}

	��� RxJava ʹ�õ� �ӿھͶ�����ˣ�ģ���಻��Ҫ�䶯��������ֱ�ӽ�����������

 3. ��������

	ʹ�� RxJava �� Retrofit ����ֱ���� ���߳��б�д��
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_main);
		
		    Retrofit retrofit=new Retrofit.Builder()
		            .baseUrl("https://api.github.com")
		            .addConverterFactory(GsonConverterFactory.create())//��� json ת����
		            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//��� RxJava ������
		            .build();
		
		    GitHub gitHub=retrofit.create(GitHub.class);
		    gitHub.RxContributors("square","retrofit")
		            .subscribeOn(Schedulers.io())
		            .observeOn(AndroidSchedulers.mainThread())
		            .subscribe(new Subscriber<List<Contributor>>() {
		                @Override
		                public void onCompleted() {
		                    Log.i("TAG","onCompleted");
		                }
		
		                @Override
		                public void onError(Throwable e) {
		
		                }
		
		                @Override
		                public void onNext(List<Contributor> contributors) {
		                   for (Contributor c:contributors){
		                       Log.i("TAG","RxJava-->"+c.getLogin()+"  "+c.getId()+"  "+c.getContributions());
		                   }
		                }
		            });
		}

##ʹ��Retrofit��Okhttpʵ�����绺�档���������棬�������ݹ���ʱ����������

[okhttp �������ùٷ��ĵ�](https://github.com/square/okhttp/wiki/Recipes)
[ʹ��Retrofit��Okhttpʵ�����绺�档���������棬�������ݹ���ʱ����������](http://www.jianshu.com/p/9c3b4ea108a7#)

 okhttp3.X��retrofit:2.0.0-beta4����

 1. ����okhttp�е�Cache
 
		OkHttpClient okHttpClient;
		File cacheFile = new File(DemoActivity.this.getCacheDir(), "[����Ŀ¼]");
		Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //100Mb
		okHttpClient = new OkHttpClient.Builder()
		     .cache(cache)
		     .build();

 2. ��������ͷ�е�cache-control
	
	    //ʹ�� RxJava �ķ���,����һ�� Observable
	    @Headers("Cache-Control: public, max-age=3600")
	    @GET("/repos/{owner}/{repo}/contributors")
	    Observable<List<Contributor>> RxContributors(@Path("owner") String owner,@Path("repo") String repo);

 3. �������� Retrofit ���ò�ͬ�ĵײ������

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())//��� json ת����
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//��� RxJava ������
                .build();

#API

## ֪���ձ�

    //Zhihu API
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/news/";//�ձ�����
    public static final String NEWS_LATEST = "http://news-at.zhihu.com/api/4/news/latest";//�����ձ�
    public static final String NEWS_BEFORE = "http://news-at.zhihu.com/api/4/news/before/";//ָ�����ڵ��ձ�
    public static final String SPLASH = "http://news-at.zhihu.com/api/4/start-image/1080*1920";//����

 - �ձ�����
	 
	 BASE_URL+�ձ�ID
	 [http://news-at.zhihu.com/api/4/news/7942211](http://news-at.zhihu.com/api/4/news/7942211)
	 
 - �����ձ�
	 
	 ֱ�ӷ��ʣ�NEWS_LATEST
	 [http://news-at.zhihu.com/api/4/news/latest](http://news-at.zhihu.com/api/4/news/latest)
	 
 - ָ�����ڵ��ձ�
	 
	 NEWS_BEFORE+(ָ������+1��)����ʽΪȫ���֣�YYYYMMDD,�磺
	 2016��02��29�ŵ����ݣ�
	 [http://news-at.zhihu.com/api/4/news/before/20160301](http://news-at.zhihu.com/api/4/news/before/20160301)
 
## ������Ů

    public static String DB_BREAST = "http://www.dbmeinv.com/dbgroup/show.htm?cid=2&pager_offset=";
    public static String DB_BUTT = "http://www.dbmeinv.com/dbgroup/show.htm?cid=6&pager_offset=";
    public static String DB_SILK = "http://www.dbmeinv.com/dbgroup/show.htm?cid=7&pager_offset=";
    public static String DB_LEG = "http://www.dbmeinv.com/dbgroup/show.htm?cid=3&pager_offset=";
    public static String DB_RANK="http://www.dbmeinv.com/dbgroup/rank.htm?pager_offset=";

---

#MDʵ��

##�໬ NavigationView

 - [windowDrawsSystemBarBackgrounds:��ν�DrawerLayout��ʾ��ActionBar/Toolbar��status bar֮��](http://solo.farbox.com/blog/how-do-i-use-drawerlayout-to-display-over-the-actionbar-or-toolbar-and-under-the-status-bar)
 - [Android M�¿ؼ�֮AppBarLayout��NavigationView��CoordinatorLayout��CollapsingToolbarLayout��ʹ��](http://blog.csdn.net/feiduclear_up/article/details/46514791) 
 - [Android �Լ�ʵ�� NavigationView [Design Support Library(1)]](http://blog.csdn.net/lmj623565791/article/details/46405409)

##��������SwipeRefreshLayout
 
 - [Android SwipeRefreshLayout �ٷ�����ˢ�¿ؼ�����](http://blog.csdn.net/lmj623565791/article/details/24521483)
 - [RecyclerView��ȫ����֮����ˢ������������SwipeRefreshLayout](http://www.lcode.org/recyclerview%E5%AE%8C%E5%85%A8%E8%A7%A3%E6%9E%90%E4%B9%8B%E4%B8%8B%E6%8B%89%E5%88%B7%E6%96%B0%E4%B8%8E%E4%B8%8A%E6%8B%89%E5%8A%A0%E8%BD%BDswiperefreshlayout/)
 - [ʵ������֮SwipeRefreshLayout+RecyclerView+CardView](http://www.lcode.org/%E5%AE%9E%E4%BE%8B%E8%A7%A3%E6%9E%90%E4%B9%8Bswiperefreshlayoutrecyclerviewcardview/)
 - [Google�ٷ�����ˢ�����--SwipeRefreshLayout](https://github.com/stormzhang/SwipeRefreshLayoutDemo)