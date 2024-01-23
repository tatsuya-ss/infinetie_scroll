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
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.infinite_scroll.databinding.ActivityMainBinding
import com.example.infinite_scroll.databinding.RowArticleItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val articles = (1..30).map { it }.toMutableList()

    private var previousMaxY = 0

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
                    val extent = binding.infiniteRecyclerView.computeVerticalScrollExtent()
                    val offset = binding.infiniteRecyclerView.computeVerticalScrollOffset() - previousMaxY
                    val range = binding.infiniteRecyclerView.computeVerticalScrollRange()
                    val maxY = (range - extent)
                    val resizeMaxY = maxY - previousMaxY
                    val wariai = ((offset.toDouble() / resizeMaxY.toDouble()) * 100).toInt()

                    Log.d("Tatsuya ٩( ᐛ )و", "onScrolled: offset $offset extent $extent range $range maxY $maxY resizeMaxY $resizeMaxY")

                    if (wariai >= 70) {
                        val lastVisibleItemPosition = (binding.infiniteRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        Log.d("Tatsuya ٩( ᐛ )و", "onScrolled: $lastVisibleItemPosition 追加します")
                        val adapter = (binding.infiniteRecyclerView.adapter as ArticleAdapter)
                        previousMaxY = maxY + extent

                        val max = articles.max()
                        val nextArticles = (max + 1..(max + 30)).map { it }.toMutableList()
                        articles.addAll(nextArticles)

                        adapter.addArticles(articles)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }
}

class ArticleAdapter(
    private var dataSet: List<Int>,
    private val context: Context,
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_article_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(dataSet[position].toString())
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

    fun addArticles(articles: List<Int>) {
        dataSet = articles
    }
}