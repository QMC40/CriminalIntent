package com.example.budgetTracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.R
import java.util.*

private const val TAG = "ExpenseListFragment"
class ExpenseListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onExpenseSelected(expenseId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var expenseRecyclerView: RecyclerView
    private var adapter: ExpenseAdapter? = ExpenseAdapter(emptyList())

    private val expenseListViewModel: ExpenseListViewModel by lazy {
        ViewModelProviders.of(this).get(ExpenseListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list.xml, container, false)
        expenseRecyclerView =
            view.findViewById(R.id.expense_recycler_view) as RecyclerView
        expenseRecyclerView.layoutManager = LinearLayoutManager(context)
        expenseRecyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseListViewModel.expenseListLiveData.observe(
            viewLifecycleOwner,
            Observer { expenses ->
                expenses?.let {
                    Log.i(TAG, "Got expenses ${expenses.size}")
                    updateUI(expenses)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_expense_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_expense -> {
                val expense = Expense()
                expenseListViewModel.addExpense(expense)
                callbacks?.onExpenseSelected(expense.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(expenses: List<Expense>) {
        adapter = ExpenseAdapter(expenses)
        expenseRecyclerView.adapter = adapter
    }

    private inner class ExpenseHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var expense: Expense
        private val titleTextView: TextView = itemView.findViewById(R.id.expense_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.expense_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved_icon)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(expense: Expense) {
            this.expense = expense
            titleTextView.text = this.expense.title
            dateTextView.text = this.expense.date.toString()
            solvedImageView.visibility = if (expense.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        override fun onClick(v: View) {
            callbacks?.onExpenseSelected(expense.id)
        }
    }

    private inner class ExpenseAdapter(var expenses: List<Expense>)
        : RecyclerView.Adapter<ExpenseHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ExpenseHolder {
            val view = layoutInflater.inflate(R.layout.list_item_expense, parent, false)
            return ExpenseHolder(view)
        }
        override fun getItemCount() = expenses.size
        override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
            val expense = expenses[position]
            holder.bind(expense)
        }
    }
    companion object {
        fun newInstance(): ExpenseListFragment {
            return ExpenseListFragment()
        }
    }
}