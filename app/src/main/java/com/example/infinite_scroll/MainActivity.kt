package com.example.infinite_scroll

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.infinite_scroll.databinding.ActivityMainBinding
import com.example.infinite_scroll.databinding.RowArticleItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val articles = (0..30).map { it.toString() }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.infiniteRecyclerView.layoutManager = layoutManager
        val adapter = ArticleAdapter(articles, this)
        binding.infiniteRecyclerView.adapter = adapter

        binding.infiniteRecyclerView.addOnScrollListener(
            object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val extent = binding.infiniteRecyclerView.computeVerticalScrollExtent().toDouble()
                    val offset = binding.infiniteRecyclerView.computeVerticalScrollOffset().toDouble()
                    val range = binding.infiniteRecyclerView.computeVerticalScrollRange().toDouble()
                    val total = range - extent
                    val wariai = ((offset / total) * 100).toInt()

                    Log.d("Tatsuya ٩( ᐛ )و", "現在のスクロール位置の割合: $wariai")

                    if (wariai >= 60) {
                        Log.d("Tatsuya ٩( ᐛ )و", "リストを追加！！　現在のスクロール位置の割合: $wariai")
                        articles.addAll(articles)
                        val adapter = (binding.infiniteRecyclerView.adapter as ArticleAdapter)
                        adapter.addArticles(articles)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }
}

class ArticleAdapter(
    private var dataSet: List<String>,
    private val context: Context,
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_article_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bind: RowArticleItemBinding = RowArticleItemBinding.bind(itemView)

        fun set(title: String) {
            bind.textView.text = title
        }
    }

    fun addArticles(articles: List<String>) {
        dataSet = articles
    }
}